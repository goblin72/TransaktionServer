package com.transactions.reg;

import com.sigma.utils.DateString;
import com.sigma.utils.Decimal;
import com.sigma.utils.jpa.DataManager;
import com.transactions.common.EMailManager;
import com.transactions.common.TSException;
import com.transactions.common.ThreadContext;
import com.transactions.db.DataBase;
import com.transactions.input.protocol.Errors;
import com.transactions.input.protocol.request.CheckRequest;
import com.transactions.input.protocol.request.PaymentRequest;
import com.transactions.input.protocol.request.SignRequest;
import com.transactions.input.protocol.response.CheckResponse;
import com.transactions.input.protocol.response.Parameter;
import com.transactions.input.protocol.response.PaymentResponse;
import com.transactions.input.protocol.response.SignResponse;
import com.transactions.model.*;
import com.transactions.output.AdapterResponse;
import com.transactions.output.OutputAdapter;
import com.transactions.output.OutputAdapterFactory;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.persistence.EntityTransaction;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 20.04.13
 * Time: 11:50
 */
@Component
public class Registrator {

    private
    @Value("${sign.path}")
    String signPath;
    private
    @Value("${email.smtp.server}")
    String emailSever;
    private
    @Value("${email.smtp.protocol}")
    String emailProtocol;
    private
    @Value("${email.smtp.port}")
    String emailPort;
    private
    @Value("${email.smtp.user}")
    String emailUser;
    private
    @Value("${email.smtp.password}")
    String emailPassword;
    private
    @Value("${email.from}")
    String emailFrom;
    private
    @Value("${checkTemplate.path}")
    String checkTemplatePath;


    @Resource
    private DataBase dataBase;

    private static final Logger logger = Logger.getLogger(Registrator.class);

    @PostConstruct
    public void init() throws MessagingException {
        EMailManager.init(emailProtocol, emailSever, Integer.parseInt(emailPort), emailUser, emailPassword);
    }

    public CheckResponse check(CheckRequest request) throws TSException {
        try {
            logger.debug("call registartor.check with " + request);
            Point p = dataBase.getPoint(request.getPointId());
            Set<String> allowableBanks=new HashSet<String>();
            boolean validBank=false;
            if (p.getCompany().getStatus().equals(Company.Status.ENABLE))
                allowableBanks.add(p.getCompany().getCode());
            if (request.getBank() == null || request.getBank().isEmpty())
            {
                validBank=true;
                request.setBank(p.getCompany().getCode());
            } else
            {
                if (p.getCompany().getCode().equals(request.getBank()))
                    validBank=true;
            }
            Company c = dataBase.getCompany(request.getBank());
            List<Company> partners=p.getPartners();

            if (partners!=null)
                for (Company partner:partners)
                {
                    if (partner.getStatus().equals(Company.Status.ENABLE))
                        allowableBanks.add(partner.getCode());
                    if (partner.getCode().equals(c.getCode()))
                    {
                        validBank=true;
                    }
                }
            if (!validBank)
                throw new TSException(Errors.UNKNOWN_COMPANY,"Недопустимыая компания.");
            CheckResponse cr = new CheckResponse();
            cr.setAllowableBank(allowableBanks);
            cr.setBankStatus(c.getStatus().name());
            cr.setComment(c.getComment());
            cr.setContract(c.getContract());
            cr.setInn(c.getInn());
            cr.setName(c.getName());
            cr.setPointStatus(p.getStatus().name());
            cr.setError(0);
            cr.setMessage("OK");
            return cr;
        } finally {
        }
    }

    public PaymentResponse payment(PaymentRequest request) throws TSException {
        try {
            logger.debug("call registartor.payment with " + request);
            boolean validBank=false;
            Point point = dataBase.getPoint(request.getPointId());
            if (request.getBank() == null || request.getBank().isEmpty())
            {
                validBank=true;
                request.setBank(point.getCompany().getCode());
            }else
            {
                if (point.getCompany().getCode().equals(request.getBank()))
                    validBank=true;
            }
            Company bank = dataBase.getCompany(request.getBank());
            List<Company> partners=point.getPartners();
            if (partners!=null)
                for (Company partner:partners)
                    if (partner.getCode().equals(bank.getCode()))
                    {
                        validBank=true;
                        break;
                    }
            if (!validBank)
                throw new TSException(Errors.UNKNOWN_COMPANY,"Недопустимыая компания.");

            String cardNumber = getCardNumber(request.getTrack());
            Long cardType = Long.parseLong(String.valueOf(cardNumber.charAt(0)));
            Script script = dataBase.getScript(bank, cardType);
            ScriptBank bankNumber = dataBase.getScriptBank(script, cardNumber.substring(0, 6));

            if (!bank.getStatus().equals(Company.Status.ENABLE)) {
                throw new TSException(Errors.BAD_COMAPNY);
            }
            if (point.getStatus().equals(Point.Status.BLOCKED) || point.getStatus().equals(Point.Status.INACTIVE)) {
                throw new TSException(Errors.BAD_POINT);
            }

            Payment payment = new Payment();
            logger.debug("set payment PASS");
            payment.setStatus(Payment.Status.PASS);
            payment.setAmount(request.getAmount());
            payment.setScriptBank(bankNumber);
            payment.setCompany(bank);
            payment.setKsn(request.getKsn());
            Date paymentDate = new Date();
            payment.setPaymentDate(paymentDate);
            payment.setPoint(point);
            payment.setTrack2(request.getTrack());
            DataManager dm = ThreadContext.getThreadContext().getManager();
            dm.persist(payment);
            dm.flush();

            OutputAdapter adapter = OutputAdapterFactory.getOutputAdapter(bankNumber.getBank().getOutputCode());
            List<Parameter> params = new ArrayList<Parameter>();
            try {
                AdapterResponse response = adapter.execute(payment);
                for (Map.Entry<String, String> entry : response.getParams().entrySet()) {
                    params.add(new Parameter(entry));
                }
                params.add(new Parameter("DATE", DateString.convert(paymentDate, "dd.MM.yyyy HH:mm:ss")));
                params.add(new Parameter("AMOUNT", Decimal.toString(Decimal.fromCents(request.getAmount()))));
                params.add(new Parameter("CONTRACT", bank.getContract()));  //point.getCompany() было
                params.add(new Parameter("DOCUMENT", request.getDocument()));
                params.add(new Parameter("INN", bank.getInn()));
                params.add(new Parameter("NAME", bank.getName()));
                params.add(new Parameter("COMMENT", bank.getComment()));

                for (Parameter entry : params) {
                    PaymentParam par = new PaymentParam();
                    par.setKey(entry.getKey());
                    par.setValue(entry.getValue());
                    par.setPayment(payment);
                    dm.persist(par);
                }
                logger.debug("set payment NOT_CONFIRMED");
                payment.setStatus(Payment.Status.NOT_CONFIRMED);
                dm.merge(payment);
                dm.commit();
            } catch (TSException e) {
                logger.debug("set payment REJECTED");
                payment.setStatus(Payment.Status.REJECTED);
                dm.merge(payment);
                dm.commit();
                throw e;
            }
            PaymentResponse resp = new PaymentResponse();
            resp.setId(payment.getId());
            resp.setParameters(params);
            resp.setError(0);
            resp.setMessage("OK");
            return resp;
        } finally {
        }
    }

    public SignResponse sign(SignRequest request) throws TSException {
        try {
            SignResponse response = new SignResponse();
            try {
                logger.debug("call registartor.sign with " + request);
                logger.debug("sign.path=" + signPath);
                Payment pay = dataBase.getPayment(request.getPaymentId());
                if (!pay.getStatus().equals(Payment.Status.NOT_CONFIRMED))
                    throw new TSException(Errors.UNKNOWN, "Не верный статус платежа: " + pay.getStatus() + ". Необходим NOT_CONFIRMED.");
                String signFilePath = signPath + "/" + request.getPaymentId() + ".png";
                FileOutputStream fos = new FileOutputStream(signFilePath);
                fos.write(Hex.decodeHex(request.getImg().toCharArray()));
                fos.flush();
                fos.close();

                pay.setStatus(Payment.Status.POST);
                pay.setEmail(request.getMail());
                DataManager dm = ThreadContext.getThreadContext().getManager();
                dm.merge(pay);
                dm.flush();
                if (request.getMail() != null || pay.getCompany().getEmail()!=null || pay.getPoint().getCompany().getEmail()!=null) {
                    String check = IOUtils.toString(new FileInputStream(checkTemplatePath + "/" + pay.getScriptBank().getBank().getCheckTemplate()));
                    for (PaymentParam param : pay.getParameters()) {
                        check = check.replace("$" + param.getKey(), param.getValue());
                    }

                    List<File> attachment = new ArrayList<File>();
                    File signFile = new File(signFilePath);
                    attachment.add(signFile);
                    check = check.replace("$SIGN", "cid:" + signFile.getName());
                    
                    StringBuilder recipients = new StringBuilder();
                    if (request.getMail()!=null)
                        recipients.append(request.getMail());

                    if (pay.getCompany().getEmail()!=null)
                    {
                        if (recipients.length()>0)
                            recipients.append(",");
                        recipients.append(pay.getCompany().getEmail());
                    }
                    if (pay.getPoint().getCompany().getEmail()!=null && !pay.getPoint().getCompany().getCode().equals(pay.getCompany().getCode()))
                    {
                        if (recipients.length()>0)
                            recipients.append(",");
                        recipients.append(pay.getPoint().getCompany().getEmail());
                    }
                    try{
                        EMailManager.sendHTMLEmail2("Копия чека", check, emailFrom, recipients.toString(), attachment);
                    }catch (Exception e)
                    {
                        logger.error(e,e);
                    }
                }
                response.setError(0);
                response.setMessage("OK");
            } catch (IOException e) {
                response.setError(Errors.UNKNOWN.getCode());
                response.setMessage("Ошибка ввода/вывода. " + e.getMessage());
            } catch (DecoderException e) {
                response.setError(Errors.UNKNOWN.getCode());
                response.setMessage("Ошибка декодирования файла подписи. " + e.getMessage());
            }

            return response;
        } finally {
        }
    }

    /**
     * @param track ;XXXXXXXXXXXXXXXX=YYYYYYYYYYYYYYYYY?Z, 37 символов
     *              X...X - номер карты, Z - LRC
     * @return номер карты
     */

    private String getCardNumber(String track) {
        String[] strs = track.split("\\=");
        return strs[0].substring(1);
    }

}

create database test2 character set utf8;


ALTER TABLE script_bank ADD UNIQUE (bank_id, script_id);
select distinct * from information_schema.TABLE_CONSTRAINTS where CONSTRAINT_SCHEMA = 'test2' and table_name='script_bank';
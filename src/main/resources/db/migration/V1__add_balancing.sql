ALTER TABLE ENERGY_GRID ADD COLUMN reporting_enabled BOOLEAN;
ALTER TABLE ENERGY_GRID ADD COLUMN current_period_balance_sum DECIMAL(19, 10);
ALTER TABLE ENERGY_GRID ADD COLUMN current_period_end TIMESTAMP;

UPDATE ENERGY_GRID set reporting_enabled = true;
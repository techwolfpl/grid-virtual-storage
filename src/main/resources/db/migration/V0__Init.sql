CREATE TABLE ENERGY_STORAGE (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    charged_from DATE,
    charged_to DATE,
    discharged_from DATE,
    discharged_to DATE,
    energy DECIMAL
);

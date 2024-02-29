CREATE TABLE ENERGY_GRID
(
    id                        UUID NOT NULL,
    name                      VARCHAR(255),
    forward_energy            DECIMAL(19, 10),
    reverse_energy            DECIMAL(19, 10),
    real_forward_energy       DECIMAL(19, 10),
    real_reverse_energy       DECIMAL(19, 10),
    energy_charged_to_storage DECIMAL(19, 10),
    energy_used_from_storage  DECIMAL(19, 10),
    PRIMARY KEY (id)
);

CREATE TABLE ENERGY_STORAGE
(
    id                       UUID NOT NULL,
    name                     VARCHAR(255),
    charged_from             DATE,
    charged_to               DATE,
    discharged_from          DATE,
    discharged_to            DATE,
    energy                   DECIMAL(19, 10),
    charge_efficiency_factor DECIMAL(19, 10),
    energy_grid_id           UUID,
    PRIMARY KEY (id),
    FOREIGN KEY (energy_grid_id) REFERENCES ENERGY_GRID (id)
);
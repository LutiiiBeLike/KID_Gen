-- This development migration intentionally removes the obsolete KID data model.
DROP TABLE IF EXISTS generated_kid;
DROP TABLE IF EXISTS kid_counter;

CREATE TABLE cid_counter (
    range_letter VARCHAR(1) PRIMARY KEY,
    counter BIGINT NOT NULL CHECK (counter >= 0),
    CHECK (range_letter >= 'A' AND range_letter <= 'Z')
);

-- The counter is the next decimal value to generate, so the first CID is CD0000.
INSERT INTO cid_counter (range_letter, counter) VALUES ('D', 0);

CREATE TABLE generated_cid (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cid VARCHAR(6) NOT NULL UNIQUE,
    hr_system VARCHAR(255) NOT NULL,
    eon_accounting_area_id VARCHAR(255) NOT NULL,
    employee_number VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

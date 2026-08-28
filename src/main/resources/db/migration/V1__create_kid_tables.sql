CREATE TABLE kid_counter (
    letter CHAR(1) PRIMARY KEY,
    counter BIGINT NOT NULL CHECK (counter >= 0),
    CHECK (letter >= 'A' AND letter <= 'Z')
);

INSERT INTO kid_counter (letter, counter) VALUES
    ('A', 0), ('B', 0), ('C', 0), ('D', 0), ('E', 0), ('F', 0), ('G', 0),
    ('H', 0), ('I', 0), ('J', 0), ('K', 0), ('L', 0), ('M', 0), ('N', 0),
    ('O', 0), ('P', 0), ('Q', 0), ('R', 0), ('S', 0), ('T', 0), ('U', 0),
    ('V', 0), ('W', 0), ('X', 0), ('Y', 0), ('Z', 0);

CREATE TABLE generated_kid (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    kid VARCHAR(32) NOT NULL UNIQUE,
    sn VARCHAR(255) NOT NULL,
    given_name VARCHAR(255) NOT NULL,
    eon_bu_short VARCHAR(255) NOT NULL,
    eon_user_type VARCHAR(255) NOT NULL,
    eon_user_purpose VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMPTZ NOT NULL
);

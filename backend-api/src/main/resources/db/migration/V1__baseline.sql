CREATE SEQUENCE IF NOT EXISTS revinfo_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE revinfo
(
    rev      BIGINT NOT NULL,
    revtstmp BIGINT NOT NULL,
    CONSTRAINT pk_revinfo PRIMARY KEY (rev)
);

CREATE TABLE revchanges
(
    rev        BIGINT       NOT NULL,
    entityname VARCHAR(255) NOT NULL,
    CONSTRAINT pk_revchanges PRIMARY KEY (rev, entityname),
    CONSTRAINT fk_revchanges_revinfo
        FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

CREATE INDEX idx_revchanges_rev
    ON revchanges (rev);
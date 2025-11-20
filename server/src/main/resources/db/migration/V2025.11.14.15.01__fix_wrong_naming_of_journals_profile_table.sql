CREATE TABLE journals_profile
(
    journal_id BIGINT NOT NULL,
    profile_id BIGINT NOT NULL,
    CONSTRAINT pk_journals_profile PRIMARY KEY (journal_id)
);

INSERT INTO journals_profile (journal_id, profile_id)
SELECT profile_id, journal_id
FROM profile_journals;

ALTER TABLE profile_journals
    DROP CONSTRAINT fk_projou_on_journal;

ALTER TABLE profile_journals
    DROP CONSTRAINT fk_projou_on_profile;

ALTER TABLE journals_profile
    ADD CONSTRAINT fk_joupro_on_journal FOREIGN KEY (journal_id) REFERENCES journals (id);

ALTER TABLE journals_profile
    ADD CONSTRAINT fk_joupro_on_profile FOREIGN KEY (profile_id) REFERENCES profiles (id);

DROP TABLE profile_journals CASCADE;
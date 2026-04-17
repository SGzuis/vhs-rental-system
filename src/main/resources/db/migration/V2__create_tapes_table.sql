-- V2__create_tapes_table.sql
CREATE TABLE tapes (
    id VARCHAR(36) PRIMARY KEY,
    movie_id VARCHAR(36) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP,
    CONSTRAINT fk_tape_movie FOREIGN KEY (movie_id) REFERENCES movies(id)
);

-- Update rentals to point to tapes instead of movies
ALTER TABLE rentals DROP CONSTRAINT fk_rental_movie;
ALTER TABLE rentals RENAME COLUMN movie_id TO tape_id;
ALTER TABLE rentals ADD CONSTRAINT fk_rental_tape FOREIGN KEY (tape_id) REFERENCES tapes(id);
ALTER TABLE rentals ADD COLUMN rewound BOOLEAN DEFAULT true;

-- Insert a tape for existing movies (if any) to keep data consistency if needed
-- For a new system this is just schema preparation.

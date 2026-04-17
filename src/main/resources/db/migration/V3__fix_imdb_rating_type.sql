-- V3__fix_imdb_rating_type.sql
-- Corrigir tipo da coluna imdb_rating de DECIMAL para DOUBLE PRECISION para alinhar com o tipo Double do Java
ALTER TABLE movies ALTER COLUMN imdb_rating TYPE DOUBLE PRECISION USING imdb_rating::DOUBLE PRECISION;

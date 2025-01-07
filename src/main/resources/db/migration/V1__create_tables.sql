-- Table for Artists
CREATE TABLE artist (
                        id SERIAL PRIMARY KEY,
                        discogs_id BIGINT NOT NULL UNIQUE,
                        name VARCHAR(250),
                        discogs_resource_url VARCHAR(250),
                        discogs_uri VARCHAR(250),
                        discogs_releases_url VARCHAR(250),
                        name_variations VARCHAR(250)[],
                        urls VARCHAR(500)[],
                        profile TEXT,
                        data_quality VARCHAR(50)
);

-- Table for Images
CREATE TABLE artist_image (
                              id SERIAL PRIMARY KEY,
                              artist_id INT NOT NULL REFERENCES artist(id) ON DELETE CASCADE,
                              type VARCHAR(50),
                              discogs_uri VARCHAR(1024),
                              discogs_resource_url VARCHAR(1024),
                              discogs_uri150 VARCHAR(1024),
                              width INT,
                              height INT
);

-- Table for Members
CREATE TABLE artist_member (
                               id SERIAL PRIMARY KEY,
                               artist_id INT NOT NULL REFERENCES artist(id) ON DELETE CASCADE,
                               discogs_artist_id INT NOT NULL,
                               name VARCHAR(250),
                               discogs_resource_url VARCHAR(1024),
                               active BOOLEAN,
                               thumbnail_url VARCHAR(1024)
);

-- Table for Discogs Releases
CREATE TABLE release (
                         id SERIAL PRIMARY KEY,
                         discogs_id INT NOT NULL,
                         status VARCHAR(250),
                         year INT,
                         resource_url VARCHAR(1024),
                         uri VARCHAR(1024),
                         format_quantity INT,
                         title VARCHAR(250),
                         country VARCHAR(250),
                         released VARCHAR(250),
                         notes TEXT,
                         thumb VARCHAR(1024),
                         estimated_weight INT,
                         blocked_from_sale BOOLEAN
);

-- Artists table for releases
CREATE TABLE release_artist (
                                id SERIAL PRIMARY KEY,
                                release_id INT NOT NULL REFERENCES release(id) ON DELETE CASCADE,
                                discogs_artist_id INT NOT NULL,
                                name VARCHAR(250) NOT NULL,
                                anv VARCHAR(250),
                                ajoin VARCHAR(250),
                                role VARCHAR(250),
                                tracks VARCHAR(250),
                                discogs_resource_url VARCHAR(1024),
                                discogs_thumbnail_url VARCHAR(1024)
);

-- Labels table for releases
CREATE TABLE release_label (
                               id SERIAL PRIMARY KEY,
                               release_id INT NOT NULL REFERENCES release(id) ON DELETE CASCADE,
                               name VARCHAR(250) NOT NULL,
                               catno VARCHAR(250),
                               entity_type VARCHAR(50),
                               entity_type_name VARCHAR(250),
                               discogs_resource_url VARCHAR(1024)
);

-- Formats table for releases
CREATE TABLE release_format (
                                id SERIAL PRIMARY KEY,
                                release_id INT NOT NULL REFERENCES release(id) ON DELETE CASCADE,
                                name VARCHAR(250) NOT NULL,
                                qty VARCHAR(50)
);

-- Genres table for releases
CREATE TABLE release_genre (
                               id SERIAL PRIMARY KEY,
                               release_id INT NOT NULL REFERENCES release(id) ON DELETE CASCADE,
                               name VARCHAR(250) NOT NULL
);

-- Styles table for releases
CREATE TABLE release_style (
                               id SERIAL PRIMARY KEY,
                               release_id INT NOT NULL REFERENCES release(id) ON DELETE CASCADE,
                               name VARCHAR(250) NOT NULL
);

-- Tracks table for releases
CREATE TABLE release_track (
                               id SERIAL PRIMARY KEY,
                               release_id INT NOT NULL REFERENCES release(id) ON DELETE CASCADE,
                               position VARCHAR(50),
                               type VARCHAR(50),
                               title VARCHAR(250),
                               duration VARCHAR(50)
);

-- Images table for releases
CREATE TABLE release_image (
                               id SERIAL PRIMARY KEY,
                               release_id INT NOT NULL REFERENCES release(id) ON DELETE CASCADE,
                               type VARCHAR(50),
                               discogs_uri VARCHAR(1024),
                               discogs_resource_url VARCHAR(1024),
                               uri150 VARCHAR(1024),
                               width INT,
                               height INT
);

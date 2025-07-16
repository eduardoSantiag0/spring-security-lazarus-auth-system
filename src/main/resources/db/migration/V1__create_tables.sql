CREATE TABLE IF NOT EXISTS missions (
    mission_id SERIAL PRIMARY KEY,
    classification varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    mission_code int NOT NULL UNIQUE,
    mission_status varchar(255) NOT NULL,
    planet_name varchar(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    mission_code int NOT NULL,
    password varchar(255) NOT NULL,
    role varchar(255) NOT NULL,
    username varchar(255) NOT NULL UNIQUE
);

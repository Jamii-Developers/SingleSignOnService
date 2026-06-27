-- Initial schema creation for JamiiX application
-- Based on JPA entity definitions

-- User login table
CREATE TABLE user_login (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email_address VARCHAR(200) NOT NULL UNIQUE,
    password_salt VARCHAR(200) NOT NULL,
    user_key VARCHAR(1000) NOT NULL,
    active INTEGER NOT NULL,
    privacy INTEGER NOT NULL,
    date_created TIMESTAMP(6) NOT NULL,
    aux_data TEXT
);

-- User data table
CREATE TABLE user_data (
    id SERIAL PRIMARY KEY,
    user_login_id INTEGER NOT NULL UNIQUE,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    middle_name VARCHAR(100),
    address_1 VARCHAR(200),
    address_2 VARCHAR(200),
    city VARCHAR(50),
    state VARCHAR(50),
    province VARCHAR(50),
    country VARCHAR(20),
    zipcode VARCHAR(30),
    last_updated TIMESTAMP(6) NOT NULL,
    aux_data TEXT,
    CONSTRAINT fk_user_data_user_login FOREIGN KEY (user_login_id) REFERENCES user_login(id)
);

-- Device information table
CREATE TABLE device_information (
    id SERIAL PRIMARY KEY,
    user_login_id INTEGER NOT NULL,
    device_name VARCHAR(200),
    device_key VARCHAR(200),
    location VARCHAR(50),
    last_connected TIMESTAMP(6),
    active INTEGER,
    aux_data TEXT,
    CONSTRAINT fk_device_user_login FOREIGN KEY (user_login_id) REFERENCES user_login(id) ON DELETE CASCADE
);

-- User cookies table
CREATE TABLE user_cookies (
    id SERIAL PRIMARY KEY,
    user_login_id INTEGER NOT NULL,
    device_information_id INTEGER NOT NULL,
    date_created TIMESTAMP(6),
    session_key VARCHAR(255),
    expire_date TIMESTAMP(6),
    active BOOLEAN,
    aux_data TEXT,
    CONSTRAINT fk_cookies_user_login FOREIGN KEY (user_login_id) REFERENCES user_login(id) ON DELETE CASCADE,
    CONSTRAINT fk_cookies_device FOREIGN KEY (device_information_id) REFERENCES device_information(id)
);

-- Password hash records table
CREATE TABLE password_hash_records (
    id SERIAL PRIMARY KEY,
    user_login_id INTEGER NOT NULL,
    password_salt VARCHAR(1000) NOT NULL,
    date_added TIMESTAMP(6) NOT NULL,
    aux_data TEXT,
    CONSTRAINT fk_password_user_login FOREIGN KEY (user_login_id) REFERENCES user_login(id) ON DELETE CASCADE
);

-- Password hash records history table
CREATE TABLE password_hash_records_history (
    id SERIAL PRIMARY KEY,
    password_hash_record_id INTEGER NOT NULL,
    password_salt VARCHAR(1000) NOT NULL,
    date_added TIMESTAMP(6) NOT NULL,
    aux_data TEXT,
    CONSTRAINT fk_password_history FOREIGN KEY (password_hash_record_id) REFERENCES password_hash_records(id)
);

-- User groups table
CREATE TABLE user_groups (
    id SERIAL PRIMARY KEY,
    user_login_id INTEGER NOT NULL,
    aux_data TEXT,
    CONSTRAINT fk_groups_user_login FOREIGN KEY (user_login_id) REFERENCES user_login(id) ON DELETE CASCADE
);

-- User roles table
CREATE TABLE user_roles (
    id SERIAL PRIMARY KEY,
    user_login_id INTEGER NOT NULL,
    user_groups_id INTEGER NOT NULL,
    aux_data TEXT,
    CONSTRAINT fk_roles_user_login FOREIGN KEY (user_login_id) REFERENCES user_login(id) ON DELETE CASCADE,
    CONSTRAINT fk_roles_user_groups FOREIGN KEY (user_groups_id) REFERENCES user_groups(id)
);

-- User block list table
CREATE TABLE user_block_list (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    blocked_id INTEGER NOT NULL,
    status INTEGER NOT NULL,
    date_updated TIMESTAMP(6) NOT NULL,
    aux_data TEXT,
    CONSTRAINT fk_block_user FOREIGN KEY (user_id) REFERENCES user_login(id) ON DELETE CASCADE,
    CONSTRAINT fk_block_blocked FOREIGN KEY (blocked_id) REFERENCES user_login(id) ON DELETE CASCADE
);

-- User relationship table
CREATE TABLE user_relationship (
    id SERIAL PRIMARY KEY,
    sender_id INTEGER NOT NULL,
    receiver_id INTEGER NOT NULL,
    type INTEGER NOT NULL,
    status INTEGER NOT NULL,
    date_updated TIMESTAMP(6) NOT NULL,
    aux_data TEXT,
    CONSTRAINT fk_rel_sender FOREIGN KEY (sender_id) REFERENCES user_login(id) ON DELETE CASCADE,
    CONSTRAINT fk_rel_receiver FOREIGN KEY (receiver_id) REFERENCES user_login(id) ON DELETE CASCADE
);

-- User requests table
CREATE TABLE user_requests (
    id SERIAL PRIMARY KEY,
    sender_id INTEGER NOT NULL,
    receiver_id INTEGER NOT NULL,
    request_type INTEGER NOT NULL,
    status INTEGER NOT NULL,
    date_updated TIMESTAMP(6) NOT NULL,
    aux_data TEXT,
    CONSTRAINT fk_req_sender FOREIGN KEY (sender_id) REFERENCES user_login(id) ON DELETE CASCADE,
    CONSTRAINT fk_req_receiver FOREIGN KEY (receiver_id) REFERENCES user_login(id) ON DELETE CASCADE
);

-- File table owner table
CREATE TABLE file_table_owner (
    id SERIAL PRIMARY KEY,
    user_login_id INTEGER NOT NULL,
    file_type VARCHAR(200) NOT NULL,
    system_filename VARCHAR(200) NOT NULL,
    file_location VARCHAR(200) NOT NULL,
    original_filename VARCHAR(200) NOT NULL,
    file_size BIGINT NOT NULL,
    status INTEGER NOT NULL,
    date_created TIMESTAMP(6),
    last_updated TIMESTAMP(6) NOT NULL,
    aux_data TEXT,
    CONSTRAINT fk_file_owner_user_login FOREIGN KEY (user_login_id) REFERENCES user_login(id) ON DELETE CASCADE
);

-- File directory table
CREATE TABLE file_directory (
    id SERIAL PRIMARY KEY,
    user_login_id INTEGER NOT NULL,
    file_table_owner_id INTEGER NOT NULL,
    ui_directory VARCHAR(500) NOT NULL,
    last_updated TIMESTAMP(6) NOT NULL,
    aux_data TEXT,
    CONSTRAINT fk_dir_user_login FOREIGN KEY (user_login_id) REFERENCES user_login(id) ON DELETE CASCADE,
    CONSTRAINT fk_dir_file_owner FOREIGN KEY (file_table_owner_id) REFERENCES file_table_owner(id)
);

-- User data history table
CREATE TABLE user_data_history (
    id SERIAL PRIMARY KEY,
    user_data_id INTEGER NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    middle_name VARCHAR(100),
    address_1 VARCHAR(200),
    address_2 VARCHAR(200),
    city VARCHAR(50),
    state VARCHAR(50),
    province VARCHAR(50),
    country VARCHAR(20),
    zipcode VARCHAR(30),
    last_updated TIMESTAMP(6) NOT NULL,
    aux_data TEXT,
    CONSTRAINT fk_data_history FOREIGN KEY (user_data_id) REFERENCES user_data(id) ON DELETE CASCADE
);

-- Client communication table
CREATE TABLE client_communication (
    id SERIAL PRIMARY KEY,
    user_login_id INTEGER NOT NULL,
    type_of_thought INTEGER,
    date_of_thought TIMESTAMP(6),
    client_thoughts TEXT,
    aux_data TEXT,
    CONSTRAINT fk_comm_user_login FOREIGN KEY (user_login_id) REFERENCES user_login(id)
);

-- API error logs table
CREATE TABLE api_error_logs (
    id SERIAL PRIMARY KEY,
    user_login_id INTEGER NOT NULL,
    error_type INTEGER NOT NULL,
    class_name VARCHAR(255) NOT NULL,
    error_message VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP(6) NOT NULL,
    last_updated TIMESTAMP(6) NOT NULL,
    status INTEGER NOT NULL,
    aux_data TEXT,
    CONSTRAINT fk_error_user_login FOREIGN KEY (user_login_id) REFERENCES user_login(id)
);

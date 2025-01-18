CREATE TABLE notifications (id bigint not null auto_increment
    , acknowledged_at datetime(6)
    , created_at datetime(6)
    , description varchar(255)
    , icon varchar(255)
    , image varchar(255)
    , has_been_read bit
    , title varchar(255)
    , should_use_router bit
    , user_id bigint
    , primary key (id)) engine=InnoDB;

ALTER TABLE notifications ADD version TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL;

CREATE TABLE users (id bigint not null auto_increment
    , email varchar(255)
    , fullname varchar(255)
    , assigned_groups varchar(255)
    , is_active bit
    , username varchar(255)
    , primary key (id)) engine=InnoDB;


ALTER TABLE users ADD version TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL;

ALTER TABLE notifications ADD CONSTRAINT FK9y21adhxn0ayjhfocscqox7bh FOREIGN KEY (user_id) REFERENCES users (id);

-- indices based on the queries that are run on the table
CREATE INDEX users_active_email_IDX USING BTREE ON users (is_active,email);

CREATE INDEX users_active_username_IDX USING BTREE ON users (is_active,username);

CREATE INDEX users_active_assigned_groups_IDX USING BTREE ON users (is_active,assigned_groups);
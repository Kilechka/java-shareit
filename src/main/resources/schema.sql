DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS requests CASCADE;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512),
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  description VARCHAR(999) NOT NULL,
  requestor_id BIGINT NOT NULL,
  created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT pk_request PRIMARY KEY (id),
  CONSTRAINT fk_request_requestor_id FOREIGN KEY (requestor_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(999) NOT NULL,
  is_available boolean NOT NULL,
  owner_id BIGINT NOT NULL,
  request_id BIGINT,
  CONSTRAINT pk_item PRIMARY KEY (id),
  CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES users(id),
  CONSTRAINT fk_request FOREIGN KEY (request_id) REFERENCES requests(id)
);

CREATE TABLE IF NOT EXISTS bookings (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  item_id BIGINT NOT NULL,
  booker_id BIGINT NOT NULL,
  status VARCHAR(24) NOT NULL,
  CONSTRAINT pk_booking PRIMARY KEY (id),
  CONSTRAINT fk_bookings_user_id FOREIGN KEY (booker_id) REFERENCES users(id),
  CONSTRAINT fk_bookings_item_id FOREIGN KEY (item_id) REFERENCES items(id)
);

    CREATE TABLE IF NOT EXISTS comments (
      id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
      text VARCHAR(999) NOT NULL,
      item_id BIGINT NOT NULL,
      author_id BIGINT NOT NULL,
      created TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
      CONSTRAINT pk_comment PRIMARY KEY (id),
      CONSTRAINT fk_comments_item_id FOREIGN KEY (item_id) REFERENCES items(id),
      CONSTRAINT fk_comments_author_id FOREIGN KEY (author_id) REFERENCES users(id)
    );
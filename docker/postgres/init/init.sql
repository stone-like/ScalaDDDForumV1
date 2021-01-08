CREATE TABLE users (
  id   text primary key,
  name text not null,
  email text not null,
  password text not null
);
CREATE TABLE posts (
  id text primary key,
  title text not null,
  content text not null,
  user_id   text not null,
  foreign key(user_id) references users(id)
)
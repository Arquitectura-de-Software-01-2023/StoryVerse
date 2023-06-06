-- Image Database
-- docker run --name storyverse -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres
-- Database
CREATE DATABASE storyverse;
-- tables
-- Table: announcement
CREATE TABLE announcement (announcement_id SERIAL PRIMARY KEY,user_id varchar(255) NOT NULL,description varchar(100) NOT NULL,date timestamp NOT NULL,status boolean NOT NULL);
-- Table: category
CREATE TABLE category (category_id SERIAL PRIMARY KEY,name varchar(80)  NOT NULL,status boolean  NOT NULL);
INSERT INTO category (name,status) VALUES ('Aventura',true);
INSERT INTO category (name,status) VALUES ('Historia corta',true);
INSERT INTO category (name,status) VALUES ('Paranormal',true);
INSERT INTO category (name,status) VALUES ('Acción',true);
INSERT INTO category (name,status) VALUES ('Humor',true);
INSERT INTO category (name,status) VALUES ('Romance',true);
INSERT INTO category (name,status) VALUES ('Ciencia Ficción',true);
INSERT INTO category (name,status) VALUES ('Misterio',true);
INSERT INTO category (name,status) VALUES ('Suspenso',true);
INSERT INTO category (name,status) VALUES ('Espiritual',true);
INSERT INTO category (name,status) VALUES ('Novela histórica',true);
INSERT INTO category (name,status) VALUES ('Terror',true);
INSERT INTO category (name,status) VALUES ('Fantasía',true);
INSERT INTO category (name,status) VALUES ('Novela juvenil',true);
INSERT INTO category (name,status) VALUES ('Vampiros',true);
INSERT INTO category (name,status) VALUES ('Otro',true);
-- Table: chapter
CREATE TABLE chapter (chapter_id SERIAL PRIMARY KEY,story_id serial  NOT NULL,title varchar(100)  NOT NULL,description varchar(10000)  NOT NULL,publication_date timestamp  NOT NULL,status boolean  NOT NULL);
-- Table: comment
CREATE TABLE comment (comment_id SERIAL PRIMARY KEY,user_id varchar(255)  NOT NULL,chapter_id serial  NOT NULL,description varchar(400)  NOT NULL,date timestamp  NOT NULL,status boolean  NOT NULL);
-- Table: follower
CREATE TABLE follower (follow_id SERIAL PRIMARY KEY,user_id varchar(255)  NOT NULL,follower_id varchar(30)  NOT NULL,date date  NOT NULL,state boolean  NOT NULL);
-- Table: library
CREATE TABLE library (library_id SERIAL PRIMARY KEY,user_id varchar(255)  NOT NULL,story_id serial  NOT NULL,date date  NOT NULL,status boolean  NOT NULL);
-- Table: notification
CREATE TABLE notification (notification_id SERIAL PRIMARY KEY,user_id varchar(255)  NOT NULL,title varchar(80)  NOT NULL,description varchar(100)  NOT NULL,date timestamp  NOT NULL,status boolean  NOT NULL);
-- Table: story
CREATE TABLE story (story_id SERIAL PRIMARY KEY,writer_id serial  NOT NULL,category_id serial  NOT NULL,title varchar(80)  NOT NULL,url_cover varchar(400)  NOT NULL,description varchar(400)  NOT NULL,audience varchar(100)  NOT NULL,language varchar(15)  NOT NULL,publication_date date  NOT NULL,votes int  NOT NULL,status boolean  NOT NULL);
-- Table: story_tag
CREATE TABLE story_tag (story_tag_id SERIAL PRIMARY KEY,story_id serial  NOT NULL,tag_id serial  NOT NULL,status boolean  NOT NULL);
-- Table: tag
CREATE TABLE tag (tag_id SERIAL PRIMARY KEY,name varchar(100)  NOT NULL,status boolean  NOT NULL);
-- Table: writer
CREATE TABLE writer (writer_id SERIAL PRIMARY KEY,user_id varchar(255)  NOT NULL,date date  NOT NULL,status boolean  NOT NULL);
-- foreign keys
-- foreign keys
-- Reference: Chapter_History (table: chapter)
ALTER TABLE chapter ADD CONSTRAINT Chapter_History FOREIGN KEY (story_id) REFERENCES story (story_id) NOT DEFERRABLE INITIALLY IMMEDIATE;
-- Reference: History_Category (table: story)
ALTER TABLE story ADD CONSTRAINT History_Category FOREIGN KEY (category_id) REFERENCES category (category_id) NOT DEFERRABLE INITIALLY IMMEDIATE;
-- Reference: comment_chapter (table: comment)
ALTER TABLE comment ADD CONSTRAINT comment_chapter FOREIGN KEY (chapter_id) REFERENCES chapter (chapter_id) NOT DEFERRABLE INITIALLY IMMEDIATE;
-- Reference: library_story (table: library)
ALTER TABLE library ADD CONSTRAINT library_story FOREIGN KEY (story_id) REFERENCES story (story_id) NOT DEFERRABLE INITIALLY IMMEDIATE;
-- Reference: story_tag_story (table: story_tag)
ALTER TABLE story_tag ADD CONSTRAINT story_tag_story FOREIGN KEY (story_id) REFERENCES story (story_id) NOT DEFERRABLE INITIALLY IMMEDIATE;
-- Reference: story_tag_tag (table: story_tag)
ALTER TABLE story_tag ADD CONSTRAINT story_tag_tag FOREIGN KEY (tag_id) REFERENCES tag (tag_id) NOT DEFERRABLE INITIALLY IMMEDIATE;
-- Reference: story_writer (table: story)
ALTER TABLE story ADD CONSTRAINT story_writer FOREIGN KEY (writer_id) REFERENCES writer (writer_id) NOT DEFERRABLE INITIALLY IMMEDIATE;
-- End of file.
--
-- PostgreSQL database dump
--

-- Dumped from database version 16.2 (Ubuntu 16.2-1.pgdg23.10+1)
-- Dumped by pg_dump version 16.2 (Ubuntu 16.2-1.pgdg23.10+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE IF EXISTS tab_viewer_db;
--
-- Name: tab_viewer_db; Type: DATABASE; Schema: -; Owner: admin
--

CREATE DATABASE tab_viewer_db WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'ru_RU.UTF-8';


ALTER DATABASE tab_viewer_db OWNER TO admin;

\connect tab_viewer_db

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: review; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.review (
    tab_tab_id integer,
    uploaded_id integer,
    value integer,
    review_id integer NOT NULL
);


ALTER TABLE public.review OWNER TO admin;

--
-- Name: average_rating(integer); Type: PROCEDURE; Schema: public; Owner: admin
--

CREATE PROCEDURE public.average_rating(IN tabid integer)
    LANGUAGE sql
    BEGIN ATOMIC
 SELECT avg(r.value) AS avg
    FROM public.review r
   WHERE (r.tab_tab_id = average_rating.tabid);
END;


ALTER PROCEDURE public.average_rating(IN tabid integer) OWNER TO admin;

--
-- Name: update_avg(integer); Type: FUNCTION; Schema: public; Owner: admin
--

CREATE FUNCTION public.update_avg(id integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
        BEGIN
            UPDATE tabulature set rating = (select avg(value) from review where tab_tab_id = id) where tab_id = id;
            RETURN id;
        END;
$$;


ALTER FUNCTION public.update_avg(id integer) OWNER TO admin;

--
-- Name: update_rating(integer); Type: PROCEDURE; Schema: public; Owner: admin
--

CREATE PROCEDURE public.update_rating(IN id integer)
    LANGUAGE sql
    AS $$
    UPDATE tabulature set rating = (select avg(value) from review where tab_tab_id = id) where tab_id = id;
$$;


ALTER PROCEDURE public.update_rating(IN id integer) OWNER TO admin;

--
-- Name: favorite; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.favorite (
    favorite_id integer NOT NULL,
    user_id integer NOT NULL,
    tab_tab_id integer NOT NULL
);


ALTER TABLE public.favorite OWNER TO admin;

--
-- Name: review_id_seq; Type: SEQUENCE; Schema: public; Owner: admin
--

ALTER TABLE public.review ALTER COLUMN review_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.review_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: tab_user; Type: TABLE; Schema: public; Owner: fireg
--

CREATE TABLE public.tab_user (
    id integer NOT NULL,
    email character varying(200) NOT NULL,
    password character varying(200) NOT NULL,
    role character varying(200) NOT NULL,
    username character varying(40) NOT NULL
);


ALTER TABLE public.tab_user OWNER TO fireg;

--
-- Name: table_name_favid_seq; Type: SEQUENCE; Schema: public; Owner: admin
--

ALTER TABLE public.favorite ALTER COLUMN favorite_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.table_name_favid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: tabulature; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.tabulature (
    tab_id integer NOT NULL,
    title character varying(200) NOT NULL,
    author character varying(200) NOT NULL,
    filepath character varying NOT NULL,
    user_id integer,
    uploaded timestamp without time zone DEFAULT now(),
    last_update timestamp without time zone DEFAULT now(),
    rating integer DEFAULT 0
);


ALTER TABLE public.tabulature OWNER TO admin;

--
-- Name: tabulature_id_seq; Type: SEQUENCE; Schema: public; Owner: admin
--

ALTER TABLE public.tabulature ALTER COLUMN tab_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.tabulature_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: fireg
--

ALTER TABLE public.tab_user ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Data for Name: favorite; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.favorite (favorite_id, user_id, tab_tab_id) FROM stdin;
\.


--
-- Data for Name: review; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.review (tab_tab_id, uploaded_id, value, review_id) FROM stdin;
\.


--
-- Data for Name: tab_user; Type: TABLE DATA; Schema: public; Owner: fireg
--

COPY public.tab_user (id, email, password, role, username) FROM stdin;
0	admin@admin.com	$2a$10$eARTWfv4/NS3IyY46KL0FeF/ktQE1/BPDrlCOo9BBiEVKp1dbpwJC	ADMIN	admin
\.


--
-- Data for Name: tabulature; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.tabulature (tab_id, title, author, filepath, user_id, uploaded, last_update, rating) FROM stdin;
334	awd	awd	-1915815254animals_as_leaders-cafo.gp5	0	2024-02-09 14:27:05.573	2024-02-09 14:27:05.573	0
\.


--
-- Name: review_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.review_id_seq', 74, true);


--
-- Name: table_name_favid_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.table_name_favid_seq', 10, true);


--
-- Name: tabulature_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.tabulature_id_seq', 334, true);


--
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: fireg
--

SELECT pg_catalog.setval('public.user_id_seq', 19, true);


--
-- Name: favorite favorite_pk; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.favorite
    ADD CONSTRAINT favorite_pk PRIMARY KEY (favorite_id);


--
-- Name: review review_pk; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.review
    ADD CONSTRAINT review_pk PRIMARY KEY (review_id);


--
-- Name: tabulature tabulature_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.tabulature
    ADD CONSTRAINT tabulature_pkey PRIMARY KEY (tab_id);


--
-- Name: tab_user user_pkey; Type: CONSTRAINT; Schema: public; Owner: fireg
--

ALTER TABLE ONLY public.tab_user
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: favorite tab___fk; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.favorite
    ADD CONSTRAINT tab___fk FOREIGN KEY (tab_tab_id) REFERENCES public.tabulature(tab_id);


--
-- Name: review tabulature_fk; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.review
    ADD CONSTRAINT tabulature_fk FOREIGN KEY (tab_tab_id) REFERENCES public.tabulature(tab_id);


--
-- Name: favorite user___fk; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.favorite
    ADD CONSTRAINT user___fk FOREIGN KEY (user_id) REFERENCES public.tab_user(id);


--
-- Name: review user_fk; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.review
    ADD CONSTRAINT user_fk FOREIGN KEY (uploaded_id) REFERENCES public.tab_user(id);


--
-- Name: tabulature user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.tabulature
    ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES public.tab_user(id);


--
-- PostgreSQL database dump complete
--

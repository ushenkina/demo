CREATE TABLE measurement (
                             city_id         int not null,
                             logdate         date not null,
                             peaktemp        int,
                             unitsales       int
) PARTITION BY RANGE (logdate);

CREATE TABLE measurement_2026_01 PARTITION OF measurement
    FOR VALUES FROM ('2026-01-01') TO ('2026-02-01');

CREATE TABLE measurement_2026_02 PARTITION OF measurement
    FOR VALUES FROM ('2026-02-01') TO ('2026-03-01');

CREATE TABLE measurement_2026_03 PARTITION OF measurement
    FOR VALUES FROM ('2026-03-01') TO ('2026-04-01');

CREATE INDEX ON measurement_2026_01 (logdate);
CREATE INDEX ON measurement_2026_02 (logdate);
CREATE INDEX ON measurement_2026_03 (logdate);

INSERT INTO measurement
SELECT
    (random() * 10 + 1)::int,
    make_date(2026, (random() * 2 + 1)::int, (random() * 27 + 1)::int),
    (random() * 100)::int,
        (random() * 500)::int
FROM generate_series(1, 1000000);

-- ALTER TABLE measurement DETACH PARTITION measurement_2025_01;

-- DROP TABLE measurement_2026_01;

CREATE INDEX idx_measurement_city
    ON measurement (city_id);

EXPLAIN ANALYZE
SELECT *
FROM measurement
WHERE logdate BETWEEN '2026-03-01' AND '2026-03-22';

EXPLAIN ANALYZE
SELECT *
FROM measurement
WHERE city_id = 5;
-- Create table for tours
CREATE TABLE tours (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    origin VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    transport_type VARCHAR(50) NOT NULL,
    distance_km DECIMAL(10, 2), 
    estimated_time_minutes INTEGER,
    route_image BYTEA  -- Stores binary image data (e.g. tour map)
);

CREATE TABLE tour_logs 
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tour_id INTEGER NOT NULL,
    start_date_time TIMESTAMP NOT NULL,
    end_date_time TIMESTAMP NOT NULL,
    comment TEXT NOT NULL,
    difficulty INTEGER CHECK (difficulty BETWEEN 1 AND 5),
    distance_km DECIMAL(10, 2),
    rating INTEGER CHECK (rating BETWEEN 1 AND 5),

    CONSTRAINT fk_tour
        FOREIGN KEY (tour_id)
        REFERENCES tours(id)
        ON DELETE CASCADE
);
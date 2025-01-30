CREATE TABLE IF NOT EXISTS event_publication (
        id UUID PRIMARY KEY,
        completion_date TIMESTAMP(6) WITH TIME ZONE,
        event_type VARCHAR(255),
        listener_id VARCHAR(255),
        publication_date TIMESTAMP(6) WITH TIME ZONE,
        serialized_event VARCHAR(255)
)

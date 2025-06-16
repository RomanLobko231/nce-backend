CREATE TABLE app_log(
    id UUID PRIMARY KEY NOT NULL,
    action VARCHAR(50) NOT NULL,
    method_name VARCHAR(50) NOT NULL,
    username VARCHAR(255),
    user_id UUID,
    affected_id UUID,
    log_timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);
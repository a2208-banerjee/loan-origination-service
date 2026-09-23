CREATE TABLE outbox_event (
    id            UUID PRIMARY KEY,
    aggregate_id  UUID NOT NULL,
    event_type    VARCHAR(128) NOT NULL,
    payload       TEXT NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL,
    published_at  TIMESTAMPTZ,
    attempts      INT NOT NULL DEFAULT 0
);

-- the poller's hot query: unpublished rows, oldest first
CREATE INDEX idx_outbox_event_unpublished ON outbox_event (created_at) WHERE published_at IS NULL;

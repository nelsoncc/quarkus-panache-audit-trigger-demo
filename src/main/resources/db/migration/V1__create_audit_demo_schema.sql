-- ---------------------------------------------------------------------------
-- Reusable audit trigger function.
-- Attach to any table that follows the (created_at, created_by,
-- updated_at, updated_by) convention.
--
-- The trigger is authoritative: it always overwrites audit columns so that
-- application code cannot bypass the audit policy, even with raw SQL.
-- ---------------------------------------------------------------------------

CREATE SEQUENCE IF NOT EXISTS demo_product_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE FUNCTION app_set_audit_columns()
RETURNS trigger
LANGUAGE plpgsql
AS $$
DECLARE
    audit_user text := COALESCE(
        NULLIF(current_setting('app.user', true), ''),
        'system'
    );
BEGIN
    IF TG_OP = 'INSERT' THEN
        NEW.created_at := CURRENT_TIMESTAMP;
        NEW.created_by := audit_user;
    END IF;

    NEW.updated_at := CURRENT_TIMESTAMP;
    NEW.updated_by := audit_user;
    RETURN NEW;
END;
$$;

CREATE TABLE IF NOT EXISTS demo_product
(
    id         BIGINT       PRIMARY KEY DEFAULT nextval('demo_product_seq'),
    name       VARCHAR(255) NOT NULL,
    archived   BOOLEAN      NOT NULL DEFAULT FALSE,

    created_at TIMESTAMPTZ  NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_at TIMESTAMPTZ  NOT NULL,
    updated_by VARCHAR(255) NOT NULL
);

DROP TRIGGER IF EXISTS trg_demo_product_audit ON demo_product;
CREATE TRIGGER trg_demo_product_audit
BEFORE INSERT OR UPDATE ON demo_product
FOR EACH ROW
EXECUTE FUNCTION app_set_audit_columns();

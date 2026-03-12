CREATE INDEX idx_url_check_result_url_id
    ON url_check_result (url_id);

CREATE INDEX idx_url_check_result_checked_at
    ON url_check_result (checked_at);

CREATE INDEX idx_user_url_subscription_user
    ON user_url_subscription (user_id);
--DROP TABLE IF EXISTS category_weight;
--DROP TABLE IF EXISTS prediction_history;
--DROP TABLE IF EXISTS perfect_boxer;
--DROP TABLE IF EXISTS all_time_ranked_boxer;
--DROP TABLE IF EXISTS perfect_boxer_generation_batch;
--DROP TABLE IF EXISTS admin;

-- 1) Admin
CREATE TABLE IF NOT EXISTS admin (
                                     admin_id        INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                     username        VARCHAR(100) NOT NULL UNIQUE,
                                     password_hash   VARCHAR(255) NOT NULL,
                                     created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 2) WeightClass
CREATE TABLE IF NOT EXISTS weight_class (
                                            weight_class_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                            class_name      VARCHAR(50) NOT NULL UNIQUE,
                                            max_weight_lb   DOUBLE PRECISION NOT NULL,
                                            min_weight_lb   DOUBLE PRECISION NOT NULL,
                                            CONSTRAINT chk_weightclass_min_le_max CHECK (min_weight_lb <= max_weight_lb)
);

-- 3) Category Weight
CREATE TABLE IF NOT EXISTS category_weight (
                                                          weight_class_id INT PRIMARY KEY
                                                              REFERENCES weight_class(weight_class_id) ON DELETE RESTRICT,

                                                          physical_weight DOUBLE PRECISION NOT NULL,
                                                          technical_weight DOUBLE PRECISION NOT NULL,
                                                          tactical_weight DOUBLE PRECISION NOT NULL,
                                                          psychological_weight DOUBLE PRECISION NOT NULL,
                                                          experience_weight DOUBLE PRECISION NOT NULL,

                                                          CONSTRAINT chk_weights_nonnegative CHECK (
                                                              physical_weight >= 0 AND
                                                              technical_weight >= 0 AND
                                                              tactical_weight >= 0 AND
                                                              psychological_weight >= 0 AND
                                                              experience_weight >= 0
                                                              ),

                                                          CONSTRAINT chk_weights_sum_to_one CHECK (
                                                              ABS(
                                                                      physical_weight +
                                                                      technical_weight +
                                                                      tactical_weight +
                                                                      psychological_weight +
                                                                      experience_weight
                                                                          - 1.0
                                                              ) < 0.000001
                                                              )
);

-- 4) PredictionHistory
CREATE TABLE IF NOT EXISTS prediction_history (
                                                  prediction_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                                  boxer_a_name VARCHAR(100) NOT NULL,
                                                  boxer_b_name VARCHAR(100) NOT NULL,

                                                  predicted_winner VARCHAR(20) NOT NULL,
                                                  match_winner VARCHAR(20),
                                                  match_win_method VARCHAR(20),

                                                  weight_class_id INT NOT NULL
                                                      REFERENCES weight_class(weight_class_id) ON DELETE RESTRICT,

                                                  boxer_a_closeness_score DOUBLE PRECISION NOT NULL,
                                                  boxer_b_closeness_score DOUBLE PRECISION NOT NULL,

                                                  probability_a DOUBLE PRECISION NOT NULL,
                                                  probability_b DOUBLE PRECISION NOT NULL,

                                                  breakdown_snapshot JSONB,

                                                  prediction_date TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                                                  CONSTRAINT chk_prediction_probs_0_1 CHECK (
                                                      probability_a >= 0 AND probability_a <= 1
                                                          AND probability_b >= 0 AND probability_b <= 1
                                                      ),

                                                  CONSTRAINT chk_prediction_closeness_0_1 CHECK (
                                                      boxer_a_closeness_score >= 0 AND boxer_a_closeness_score <= 1
                                                          AND boxer_b_closeness_score >= 0 AND boxer_b_closeness_score <= 1
                                                      ),

                                                  CONSTRAINT chk_predicted_winner CHECK (
                                                      predicted_winner IN ('BOXER_A', 'BOXER_B', 'DRAW')
                                                      ),

                                                  CONSTRAINT chk_match_winner CHECK (
                                                      match_winner IS NULL
                                                          OR match_winner IN ('BOXER_A', 'BOXER_B', 'DRAW', 'NO_CONTEST')
                                                      ),

                                                  CONSTRAINT chk_match_method CHECK (
                                                      match_win_method IS NULL
                                                          OR match_win_method IN ('KO', 'TKO', 'DECISION', 'DISQUALIFICATION', 'NO_CONTEST')
                                                      ),

                                                  CONSTRAINT chk_match_winner_method_consistency CHECK (
                                                      match_winner IS NOT NULL OR match_win_method IS NULL
                                                      )
);

-- 5) Perfect Boxer Generation Batch
CREATE TABLE IF NOT EXISTS perfect_boxer_generation_batch (
                                                              batch_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                                              weight_class_id INT NOT NULL REFERENCES weight_class(weight_class_id) ON DELETE CASCADE,
                                                              amount INTEGER NOT NULL DEFAULT 10,
                                                              status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
                                                              is_active BOOLEAN NOT NULL DEFAULT TRUE,
                                                              error_message TEXT,
                                                              created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                                              CONSTRAINT chk_batch_status CHECK (status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED'))
);

-- 6) All Time Ranked Boxer
CREATE TABLE IF NOT EXISTS all_time_ranked_boxer (
                                       ranked_boxer_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                       batch_id INT NOT NULL REFERENCES perfect_boxer_generation_batch(batch_id) ON DELETE CASCADE,
                                       weight_class_id INT NOT NULL REFERENCES weight_class(weight_class_id) ON DELETE CASCADE,
                                       boxer_name VARCHAR(150) NOT NULL,
                                       ranking_position INT NOT NULL,

    -- Physical Attributes
                                       height_cm                DOUBLE PRECISION,
                                       reach_cm                DOUBLE PRECISION,
                                       weight_class_alignment  DOUBLE PRECISION,
                                       hand_speed              DOUBLE PRECISION,
                                       foot_speed              DOUBLE PRECISION,
                                       strength                DOUBLE PRECISION,
                                       endurance               DOUBLE PRECISION,
                                       reaction_time           DOUBLE PRECISION,

    -- Technical Attributes
                                       punch_accuracy              DOUBLE PRECISION,
                                       punch_variety               DOUBLE PRECISION,
                                       defensive_guard_efficiency  DOUBLE PRECISION,
                                       head_movement               DOUBLE PRECISION,
                                       footwork_technique          DOUBLE PRECISION,
                                       counterpunching_ability     DOUBLE PRECISION,
                                       combination_efficiency      DOUBLE PRECISION,

    -- Tactical Attributes
                                       ring_iq                      DOUBLE PRECISION,
                                       adaptability_mid_fight       DOUBLE PRECISION,
                                       distance_control             DOUBLE PRECISION,
                                       tempo_control                DOUBLE PRECISION,
                                       opponent_pattern_recognition DOUBLE PRECISION,
                                       fight_planning_discipline    DOUBLE PRECISION,

    -- Psychological Attributes
                                       composure_under_pressure     DOUBLE PRECISION,
                                       aggression_control           DOUBLE PRECISION,
                                       mental_toughness             DOUBLE PRECISION,
                                       focus_consistency            DOUBLE PRECISION,
                                       resilience_after_knockdown   DOUBLE PRECISION,

    -- Experience & Performance
                                       win_ratio                 DOUBLE PRECISION,
                                       knockout_ratio            DOUBLE PRECISION,
                                       title_fight_experience    DOUBLE PRECISION,
                                       strength_of_opposition    DOUBLE PRECISION,
                                       recent_fight_activity     DOUBLE PRECISION,
                                       performance_consistency   DOUBLE PRECISION,

                                       source_note TEXT,
                                       generated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                       CONSTRAINT uq_ranked_boxer_batch_rank UNIQUE (batch_id, ranking_position),
                                       CONSTRAINT uq_ranked_boxer_batch_name UNIQUE (batch_id, boxer_name)
);

-- 7) Perfect Boxer
CREATE TABLE IF NOT EXISTS perfect_boxer (
                                             perfect_boxer_id        INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                             batch_id INT NOT NULL UNIQUE REFERENCES perfect_boxer_generation_batch(batch_id) ON DELETE CASCADE,
                                             weight_class_id INT NOT NULL REFERENCES weight_class(weight_class_id) ON DELETE RESTRICT,

    -- Physical Attributes
                                             height_cm                DOUBLE PRECISION,
                                             reach_cm                DOUBLE PRECISION,
                                             weight_class_alignment  DOUBLE PRECISION,
                                             hand_speed              DOUBLE PRECISION,
                                             foot_speed              DOUBLE PRECISION,
                                             strength                DOUBLE PRECISION,
                                             endurance               DOUBLE PRECISION,
                                             reaction_time           DOUBLE PRECISION,

    -- Technical Attributes
                                             punch_accuracy              DOUBLE PRECISION,
                                             punch_variety               DOUBLE PRECISION,
                                             defensive_guard_efficiency  DOUBLE PRECISION,
                                             head_movement               DOUBLE PRECISION,
                                             footwork_technique          DOUBLE PRECISION,
                                             counterpunching_ability     DOUBLE PRECISION,
                                             combination_efficiency      DOUBLE PRECISION,

    -- Tactical Attributes
                                             ring_iq                      DOUBLE PRECISION,
                                             adaptability_mid_fight       DOUBLE PRECISION,
                                             distance_control             DOUBLE PRECISION,
                                             tempo_control                DOUBLE PRECISION,
                                             opponent_pattern_recognition DOUBLE PRECISION,
                                             fight_planning_discipline    DOUBLE PRECISION,

    -- Psychological Attributes
                                             composure_under_pressure     DOUBLE PRECISION,
                                             aggression_control           DOUBLE PRECISION,
                                             mental_toughness             DOUBLE PRECISION,
                                             focus_consistency            DOUBLE PRECISION,
                                             resilience_after_knockdown   DOUBLE PRECISION,

    -- Experience & Performance
                                             win_ratio                 DOUBLE PRECISION,
                                             knockout_ratio            DOUBLE PRECISION,
                                             title_fight_experience    DOUBLE PRECISION,
                                             strength_of_opposition    DOUBLE PRECISION,
                                             recent_fight_activity     DOUBLE PRECISION,
                                             performance_consistency   DOUBLE PRECISION,

                                             created_at               TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                             updated_at               TIMESTAMPTZ NOT NULL DEFAULT NOW()
);


CREATE UNIQUE INDEX IF NOT EXISTS uq_active_batch_per_weight_class
    ON perfect_boxer_generation_batch(weight_class_id)
    WHERE is_active = TRUE;

CREATE INDEX IF NOT EXISTS idx_batch_weight_class_created
    ON perfect_boxer_generation_batch(weight_class_id, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_ranked_boxer_batch
    ON all_time_ranked_boxer(batch_id);

CREATE INDEX IF NOT EXISTS idx_ranked_boxer_weight_class
    ON all_time_ranked_boxer(weight_class_id);

CREATE INDEX IF NOT EXISTS idx_perfect_boxer_weight_class
    ON perfect_boxer(weight_class_id);

CREATE INDEX IF NOT EXISTS idx_perfect_boxer_batch
    ON perfect_boxer(batch_id);

CREATE INDEX IF NOT EXISTS idx_prediction_weightclass_date
    ON prediction_history(weight_class_id, prediction_date DESC);

CREATE INDEX IF NOT EXISTS idx_prediction_snapshot_gin
    ON prediction_history USING GIN (breakdown_snapshot);
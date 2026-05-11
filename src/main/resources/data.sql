INSERT INTO weight_class (class_name, min_weight_lb, max_weight_lb) VALUES
                                                                        ('Minimumweight', 0.00, 105.00),
                                                                        ('Light Flyweight', 105.01, 108.00),
                                                                        ('Flyweight', 108.01, 112.00),
                                                                        ('Super Flyweight', 112.01, 115.00),
                                                                        ('Bantamweight', 115.01, 118.00),
                                                                        ('Super Bantamweight', 118.01, 122.00),
                                                                        ('Featherweight', 122.01, 126.00),
                                                                        ('Super Featherweight', 126.01, 130.00),
                                                                        ('Lightweight', 130.01, 135.00),
                                                                        ('Super Lightweight', 135.01, 140.00),
                                                                        ('Welterweight', 140.01, 147.00),
                                                                        ('Super Welterweight', 147.01, 154.00),
                                                                        ('Middleweight', 154.01, 160.00),
                                                                        ('Super Middleweight', 160.01, 168.00),
                                                                        ('Light Heavyweight', 168.01, 175.00),
                                                                        ('Cruiserweight', 175.01, 200.00),
                                                                        ('Heavyweight', 200.01, 999.00)
ON CONFLICT (class_name) DO NOTHING;

INSERT INTO category_weight (
    weight_class_id,
    physical_weight,
    technical_weight,
    tactical_weight,
    psychological_weight,
    experience_weight
)
SELECT
    wc.weight_class_id,
    v.physical_weight,
    v.technical_weight,
    v.tactical_weight,
    v.psychological_weight,
    v.experience_weight
FROM weight_class wc
         JOIN (
    VALUES
        ('Minimumweight',        0.15, 0.30, 0.25, 0.15, 0.15),
        ('Light Flyweight',      0.15, 0.30, 0.25, 0.15, 0.15),
        ('Flyweight',            0.16, 0.30, 0.24, 0.15, 0.15),
        ('Super Flyweight',      0.17, 0.29, 0.24, 0.15, 0.15),
        ('Bantamweight',         0.18, 0.28, 0.24, 0.15, 0.15),
        ('Super Bantamweight',   0.18, 0.28, 0.24, 0.15, 0.15),
        ('Featherweight',        0.19, 0.27, 0.24, 0.15, 0.15),
        ('Super Featherweight',  0.19, 0.27, 0.24, 0.15, 0.15),
        ('Lightweight',          0.20, 0.26, 0.24, 0.15, 0.15),
        ('Super Lightweight',    0.21, 0.25, 0.24, 0.15, 0.15),
        ('Welterweight',         0.22, 0.25, 0.23, 0.15, 0.15),
        ('Super Welterweight',   0.23, 0.24, 0.23, 0.15, 0.15),
        ('Middleweight',         0.24, 0.23, 0.23, 0.15, 0.15),
        ('Super Middleweight',   0.25, 0.22, 0.23, 0.15, 0.15),
        ('Light Heavyweight',    0.27, 0.21, 0.22, 0.15, 0.15),
        ('Cruiserweight',        0.29, 0.20, 0.21, 0.15, 0.15),
        ('Heavyweight',          0.32, 0.18, 0.20, 0.15, 0.15)
) AS v(
       class_name,
       physical_weight,
       technical_weight,
       tactical_weight,
       psychological_weight,
       experience_weight
    )
              ON wc.class_name = v.class_name
ON CONFLICT (weight_class_id) DO NOTHING;


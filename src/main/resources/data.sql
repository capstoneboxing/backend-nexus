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


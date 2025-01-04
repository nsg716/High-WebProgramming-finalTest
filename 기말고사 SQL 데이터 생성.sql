use wp_202020881;
-- 사용자 테이블 생성
CREATE TABLE User (
    ID VARCHAR(200) NOT NULL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(200) NOT NULL,
    password VARCHAR(200) NOT NULL,
    status VARCHAR(200) NOT NULL,
    photo VARCHAR(200) DEFAULT NULL,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 관리자 테이블 생성
CREATE TABLE Admin (
    ID VARCHAR(200) NOT NULL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(200) NOT NULL,
    password VARCHAR(200) NOT NULL,
    status VARCHAR(200) NOT NULL,
    photo VARCHAR(200) DEFAULT NULL,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 모델 테이블 생성
CREATE TABLE Model (
    model_id VARCHAR(200) NOT NULL PRIMARY KEY,
    admin_id VARCHAR(200) NOT NULL,
    model_name VARCHAR(200) NOT NULL,
    version VARCHAR(200) NOT NULL,
    model_type VARCHAR(200) NOT NULL,
    framework VARCHAR(200) NOT NULL,
    description TEXT,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES Admin(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

-- 리포트 테이블 생성
CREATE TABLE Report (
    report_id VARCHAR(200) NOT NULL PRIMARY KEY,
    model_id VARCHAR(200) NOT NULL ,
    admin_id VARCHAR(200) NOT NULL,
    user_id VARCHAR(200) NOT NULL ,
    report_name VARCHAR(200) NOT NULL,
    usage_date DATETIME NOT NULL,
    model_name VARCHAR(200) NOT NULL,
    accuracy DECIMAL(5,2) NOT NULL,
    total_processing_amount INT NOT NULL,
    average_response_time DECIMAL(10,2) NOT NULL,
    create_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (model_id) REFERENCES Model(model_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (admin_id) REFERENCES Model(admin_id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES User(ID) ON UPDATE CASCADE ON DELETE CASCADE
);




INSERT INTO User (ID, name, email, password, status, photo) VALUES
('user001', 'Alice', 'alice@example.com', 'password123!', 'active', NULL),
('user002', 'bob', 'bob@example.com', 'password123!', 'active', NULL),
('user003', 'charlie', 'charlie@example.com', 'password123!', 'active', NULL),
('user004', 'david', 'david@example.com', 'password123!', 'active', NULL),
('user005', 'eve', 'eve@example.com', 'password123!', 'active', NULL),
('user006', 'frank', 'frank@example.com', 'password123!', 'active', NULL),
('user007', 'grace', 'grace@example.com', 'password123!', 'active', NULL),
('user008', 'hank', 'hank@example.com', 'password123!', 'active', NULL),
('user009', 'ivy', 'ivy@example.com', 'password123!', 'active', NULL),
('user010', 'jack', 'jack@example.com', 'password123!', 'active', NULL);


INSERT INTO Admin (ID, name, email, password, status, photo) VALUES
('admin001', 'Admin1', 'admin1@example.com', 'adminpass', 'active', NULL),
('admin002', 'Admin2', 'admin2@example.com', 'adminpass', 'active', NULL),
('admin003', 'Admin3', 'admin3@example.com', 'adminpass', 'active', NULL);

INSERT INTO Model (model_id, admin_id, model_name, version, model_type, framework, description) VALUES
('model001', 'admin001', 'Model A', '1.0', 'Classification', 'TensorFlow', 'Classification model for images'),
('model002', 'admin002', 'Model B', '2.0', 'Regression', 'PyTorch', 'Regression model for sales prediction'),
('model003', 'admin003', 'Model C', '1.5', 'Clustering', 'Scikit-learn', 'Clustering model for customer segmentation');

INSERT INTO Report (report_id, model_id, admin_id, user_id, report_name, usage_date, model_name, accuracy, total_processing_amount, average_response_time) VALUES
('report001', 'model001', 'admin001', 'user001', 'Report A', '2024-01-01 10:00:00', 'Model A', 95.50, 100, 0.45),
('report002', 'model002', 'admin002', 'user002', 'Report B', '2024-01-02 11:00:00', 'Model B', 89.20, 200, 0.78),
('report003', 'model003', 'admin003', 'user003', 'Report C', '2024-01-03 12:00:00', 'Model C', 92.10, 150, 0.50);




select * from User;
select * from Admin;
select * from Model;
select * from Report;







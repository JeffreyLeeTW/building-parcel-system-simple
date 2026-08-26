-- Seed data for local/demo use. All statements are idempotent (safe to run on every startup).
-- Default resident password: resident123
-- Admin accounts: admin.lin / 12345678, wu.zhihao / 12345678

INSERT INTO resident (name, building_code, floor, unit, email, personal_id, password_hash)
SELECT '陳怡安','A',12,3,'yian.chen@example.com','A123456789','$2b$10$XJYo9MPg22V6McKwqVrOYOyWwXmYnFPwi4fnLUo2Lrv5Qkqq5./ve'
WHERE NOT EXISTS (SELECT 1 FROM resident WHERE email='yian.chen@example.com');

INSERT INTO resident (name, building_code, floor, unit, email, personal_id, password_hash)
SELECT '張雅婷','B',8,2,'yating.chang@example.com','B223456789','$2b$10$XJYo9MPg22V6McKwqVrOYOyWwXmYnFPwi4fnLUo2Lrv5Qkqq5./ve'
WHERE NOT EXISTS (SELECT 1 FROM resident WHERE email='yating.chang@example.com');

INSERT INTO resident (name, building_code, floor, unit, email, personal_id, password_hash)
SELECT '李冠廷','C',5,1,'kuanting.li@example.com','C323456789','$2b$10$XJYo9MPg22V6McKwqVrOYOyWwXmYnFPwi4fnLUo2Lrv5Qkqq5./ve'
WHERE NOT EXISTS (SELECT 1 FROM resident WHERE email='kuanting.li@example.com');

INSERT INTO resident (name, building_code, floor, unit, email, personal_id, password_hash)
SELECT '王子晴','A',3,2,'ziqing.wang@example.com','A423456789','$2b$10$XJYo9MPg22V6McKwqVrOYOyWwXmYnFPwi4fnLUo2Lrv5Qkqq5./ve'
WHERE NOT EXISTS (SELECT 1 FROM resident WHERE email='ziqing.wang@example.com');

INSERT INTO resident (name, building_code, floor, unit, email, personal_id, password_hash)
SELECT '林俊豪','A',7,1,'junhao.lin@example.com','A523456789','$2b$10$XJYo9MPg22V6McKwqVrOYOyWwXmYnFPwi4fnLUo2Lrv5Qkqq5./ve'
WHERE NOT EXISTS (SELECT 1 FROM resident WHERE email='junhao.lin@example.com');

INSERT INTO resident (name, building_code, floor, unit, email, personal_id, password_hash)
SELECT '黃雅雯','B',6,3,'yawen.huang@example.com','B623456789','$2b$10$XJYo9MPg22V6McKwqVrOYOyWwXmYnFPwi4fnLUo2Lrv5Qkqq5./ve'
WHERE NOT EXISTS (SELECT 1 FROM resident WHERE email='yawen.huang@example.com');

INSERT INTO resident (name, building_code, floor, unit, email, personal_id, password_hash)
SELECT '周品妤','B',15,5,'pinyu.chou@example.com','B723456789','$2b$10$XJYo9MPg22V6McKwqVrOYOyWwXmYnFPwi4fnLUo2Lrv5Qkqq5./ve'
WHERE NOT EXISTS (SELECT 1 FROM resident WHERE email='pinyu.chou@example.com');

INSERT INTO resident (name, building_code, floor, unit, email, personal_id, password_hash)
SELECT '吳承恩','C',2,6,'chengen.wu@example.com','C823456789','$2b$10$XJYo9MPg22V6McKwqVrOYOyWwXmYnFPwi4fnLUo2Lrv5Qkqq5./ve'
WHERE NOT EXISTS (SELECT 1 FROM resident WHERE email='chengen.wu@example.com');

INSERT INTO resident (name, building_code, floor, unit, email, personal_id, password_hash)
SELECT '許家豪','C',11,4,'jiahao.hsu@example.com','C923456789','$2b$10$XJYo9MPg22V6McKwqVrOYOyWwXmYnFPwi4fnLUo2Lrv5Qkqq5./ve'
WHERE NOT EXISTS (SELECT 1 FROM resident WHERE email='jiahao.hsu@example.com');

INSERT INTO resident (name, building_code, floor, unit, email, personal_id, password_hash)
SELECT '蔡佩珊','A',18,2,'peishan.tsai@example.com','A023456789','$2b$10$XJYo9MPg22V6McKwqVrOYOyWwXmYnFPwi4fnLUo2Lrv5Qkqq5./ve'
WHERE NOT EXISTS (SELECT 1 FROM resident WHERE email='peishan.tsai@example.com');

-- Backfill personal_id for residents seeded before this column existed.
UPDATE resident SET personal_id = 'A123456789' WHERE email='yian.chen@example.com' AND personal_id IS NULL;
UPDATE resident SET personal_id = 'B223456789' WHERE email='yating.chang@example.com' AND personal_id IS NULL;
UPDATE resident SET personal_id = 'C323456789' WHERE email='kuanting.li@example.com' AND personal_id IS NULL;
UPDATE resident SET personal_id = 'A423456789' WHERE email='ziqing.wang@example.com' AND personal_id IS NULL;
UPDATE resident SET personal_id = 'A523456789' WHERE email='junhao.lin@example.com' AND personal_id IS NULL;
UPDATE resident SET personal_id = 'B623456789' WHERE email='yawen.huang@example.com' AND personal_id IS NULL;
UPDATE resident SET personal_id = 'B723456789' WHERE email='pinyu.chou@example.com' AND personal_id IS NULL;
UPDATE resident SET personal_id = 'C823456789' WHERE email='chengen.wu@example.com' AND personal_id IS NULL;
UPDATE resident SET personal_id = 'C923456789' WHERE email='jiahao.hsu@example.com' AND personal_id IS NULL;
UPDATE resident SET personal_id = 'A023456789' WHERE email='peishan.tsai@example.com' AND personal_id IS NULL;

INSERT INTO parcelman (name, account, password_hash)
SELECT '林怡君','admin.lin','$2b$10$/HePzbpGGhNR/NW2vqUsHOevaBvb.rP42pyvScT26PsCyz5mQw9bS'
WHERE NOT EXISTS (SELECT 1 FROM parcelman WHERE account='admin.lin');

INSERT INTO parcelman (name, account, password_hash)
SELECT '吳志豪','wu.zhihao','$2b$10$/HePzbpGGhNR/NW2vqUsHOevaBvb.rP42pyvScT26PsCyz5mQw9bS'
WHERE NOT EXISTS (SELECT 1 FROM parcelman WHERE account='wu.zhihao');

-- Demo parcels
INSERT INTO parcel (parcel_code, status, cabinet_area, cabinet_number, resident_id, arrival_time, agent_name, agent_token)
SELECT '4826','AVAILABLE','C','18',(SELECT id FROM resident WHERE email='yian.chen@example.com'),
       TIMESTAMP '2026-08-18 10:22:00', NULL, 'seed-token-4826'
WHERE NOT EXISTS (SELECT 1 FROM parcel WHERE parcel_code='4826' AND cabinet_area='C' AND cabinet_number='18');

INSERT INTO parcel (parcel_code, status, cabinet_area, cabinet_number, resident_id, arrival_time, agent_name, agent_token)
SELECT '7319','AVAILABLE','A','07',(SELECT id FROM resident WHERE email='yian.chen@example.com'),
       TIMESTAMP '2026-08-18 09:35:00', '黃雅雯', 'seed-token-7319'
WHERE NOT EXISTS (SELECT 1 FROM parcel WHERE parcel_code='7319' AND cabinet_area='A' AND cabinet_number='07');

INSERT INTO parcel (parcel_code, status, cabinet_area, cabinet_number, resident_id, arrival_time, agent_name, agent_token)
SELECT '3504','AVAILABLE','B','11',(SELECT id FROM resident WHERE email='yating.chang@example.com'),
       TIMESTAMP '2026-08-17 16:48:00', NULL, 'seed-token-3504'
WHERE NOT EXISTS (SELECT 1 FROM parcel WHERE parcel_code='3504' AND cabinet_area='B' AND cabinet_number='11');

INSERT INTO parcel (parcel_code, status, cabinet_area, cabinet_number, resident_id, arrival_time, agent_name, agent_token)
SELECT '2291','PICKED_UP','D','03',(SELECT id FROM resident WHERE email='kuanting.li@example.com'),
       TIMESTAMP '2026-08-17 14:10:00', NULL, 'seed-token-2291'
WHERE NOT EXISTS (SELECT 1 FROM parcel WHERE parcel_code='2291' AND cabinet_area='D' AND cabinet_number='03');

INSERT INTO pickup_record (parcel_id, pickup_time, pickup_photo, handling_parcelman_id, actual_picker_name, pickup_method, signer_note)
SELECT p.id, TIMESTAMP '2026-08-17 18:32:00', NULL,
       (SELECT id FROM parcelman WHERE account='wu.zhihao'), '李冠廷', 'SELF', '由領受人本人簽收'
FROM parcel p
WHERE p.parcel_code='2291' AND p.cabinet_area='D' AND p.cabinet_number='03'
  AND NOT EXISTS (SELECT 1 FROM pickup_record pr WHERE pr.parcel_id = p.id);

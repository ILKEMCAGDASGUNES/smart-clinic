-- SmartClinic - JOIN & Aggregation Queries (Lab)

-- 1) Appointments + Doctor + Patient (JOIN)
SELECT
    a.id              AS appointment_id,
    a.date            AS appointment_date,
    a.time            AS appointment_time,
    a.status          AS status,
    d.id              AS doctor_id,
    d.name            AS doctor_name,
    d.specialization  AS doctor_specialization,
    p.id              AS patient_id,
    p.name            AS patient_name
FROM appointments a
         JOIN doctors d   ON a.doctor_id = d.id
         JOIN patients p  ON a.patient_id = p.id
ORDER BY a.date, a.time;

-- 2) COUNT: Doctor başına randevu sayısı
SELECT
    d.id   AS doctor_id,
    d.name AS doctor_name,
    COUNT(a.id) AS appointment_count
FROM doctors d
         LEFT JOIN appointments a ON a.doctor_id = d.id
GROUP BY d.id, d.name
ORDER BY appointment_count DESC;

-- 3) COUNT: Status'a göre randevu sayısı
SELECT
    a.status,
    COUNT(*) AS count_by_status
FROM appointments a
GROUP BY a.status
ORDER BY count_by_status DESC;

-- 4) MAX: En yakın (latest) randevu tarihi
SELECT MAX(a.date) AS latest_appointment_date
FROM appointments a;

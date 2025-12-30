USE smart_clinic;

-- Temiz başlamak için
DROP PROCEDURE IF EXISTS GetDailyAppointmentReportByDoctor;
DROP PROCEDURE IF EXISTS GetDoctorWithMostPatientsByMonth;
DROP PROCEDURE IF EXISTS GetDoctorWithMostPatientsByYear;

DELIMITER $$

-- 1) Günlük randevu raporu (doktor bazında gruplanmış)
CREATE PROCEDURE GetDailyAppointmentReportByDoctor(IN p_date DATE)
BEGIN
SELECT
    d.id   AS doctor_id,
    d.name AS doctor_name,
    d.specialization AS specialization,
    COUNT(a.id) AS total_appointments
FROM doctors d
         LEFT JOIN appointments a
                   ON a.doctor_id = d.id
                       AND a.date = p_date
GROUP BY d.id, d.name, d.specialization
ORDER BY total_appointments DESC, doctor_name ASC;
END$$

-- 2) Belirli ayda en çok hasta gören doktor (unique patient)
CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(IN p_year INT, IN p_month INT)
BEGIN
SELECT
    d.id   AS doctor_id,
    d.name AS doctor_name,
    COUNT(DISTINCT a.patient_id) AS unique_patients
FROM doctors d
         JOIN appointments a ON a.doctor_id = d.id
WHERE YEAR(a.date) = p_year
  AND MONTH(a.date) = p_month
  AND a.status <> 'Cancelled'
GROUP BY d.id, d.name
ORDER BY unique_patients DESC, doctor_name ASC
    LIMIT 1;
END$$

-- 3) Belirli yılda en çok hasta gören doktor (unique patient)
CREATE PROCEDURE GetDoctorWithMostPatientsByYear(IN p_year INT)
BEGIN
SELECT
    d.id   AS doctor_id,
    d.name AS doctor_name,
    COUNT(DISTINCT a.patient_id) AS unique_patients
FROM doctors d
         JOIN appointments a ON a.doctor_id = d.id
WHERE YEAR(a.date) = p_year
  AND a.status <> 'Cancelled'
GROUP BY d.id, d.name
ORDER BY unique_patients DESC, doctor_name ASC
    LIMIT 1;
END$$

DELIMITER ;

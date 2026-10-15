docker compose exec mysql mysql -u coupon -pcoupon coupon -e "
TRUNCATE TABLE issued_coupon;
TRUNCATE TABLE coupon;
TRUNCATE TABLE member;
"
k6 run script/issue.js
docker compose exec mysql mysql -u coupon -pcoupon coupon -N -B -e "
SELECT CONCAT('# of coupons: ', IFNULL(SUM(count), 0)) FROM coupon;
SELECT CONCAT('# of issued coupons: ', COUNT(*)) FROM issued_coupon;
"

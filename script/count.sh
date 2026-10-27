docker compose exec mysql mysql -u coupon -pcoupon coupon -N -B -e "
SELECT CONCAT('# of coupons: ', IFNULL(SUM(count), 0)) FROM coupon;
SELECT CONCAT('# of issued coupons: ', COUNT(*)) FROM issued_coupon;
"

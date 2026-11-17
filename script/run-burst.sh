docker compose exec redis redis-cli FLUSHDB
docker compose exec mysql mysql -u coupon -pcoupon coupon -e "
TRUNCATE TABLE issued_coupon;
TRUNCATE TABLE coupon;
TRUNCATE TABLE member;
"
k6 run script/burst.js

docker compose exec redis redis-cli FLUSHDB
docker compose exec mysql mysql -u coupon -pcoupon coupon -e "
TRUNCATE TABLE issued_coupon;
TRUNCATE TABLE coupon;
TRUNCATE TABLE member;
"
docker compose exec redis redis-cli CONFIG RESETSTAT
k6 run script/sold-out-load.js
docker compose exec redis redis-cli INFO commandstats | grep -i eval

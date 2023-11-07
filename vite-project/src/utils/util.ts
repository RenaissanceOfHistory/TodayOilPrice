/** 格式化日期 */
const formatDate = (date: Date): string => {
  const y = date.getFullYear(),
        m = date.getMonth() + 1,
        d = date.getDate(),
        h = date.getHours(),
        M = date.getMinutes(),
        s = date.getSeconds();
  return [y, m, d].map(format).join("-") + " " + [h, M, s].map(format).join(":");
};

const format = (v: any): string => {
  v = "" + v;
  return v.length <= 1 ? `0${v}` : v;
};

export default {
  formatDate,
  format
}
/**
 * @returns a string with format of "yyyy-MM-dd"
 */
export const stringfyDate = (date: Date): string => {
  return date.toISOString().slice(0, 10);
};

/**
 * @param year bettwen 1000-9999
 * @param month between 0-11 (zero indexed, so not 1-12)
 *
 * @returns a string with format of "yyyy-MM"
 */
export const stringfyYearMonth = (year: number, month: number): string => {
  return new Date(year, month).toISOString().slice(0, 7);
};

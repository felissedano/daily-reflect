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

export const checkIsValidDate = (
  yearString: string,
  monthString: string,
  dayString: string,
): boolean => {
  if (
    Number.isNaN(yearString) ||
    Number.isNaN(monthString) ||
    Number.isNaN(dayString)
  ) {
    return false;
  }

  const year = Number(yearString);
  const month = Number(monthString);
  const day = Number(dayString);

  if (year < 1900 || year > 9999) {
    return false;
  }

  if (month < 1 || month > 12) {
    return false;
  }

  if (day < 1 || day > 31) {
    return false;
  }

  return true;
};

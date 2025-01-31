import i18next, {i18n} from "i18next";
import { initReactI18next } from "react-i18next";

import transEnglish from "./i18n/en.json";
import transFrench from "./i18n/fr.json"
const resources = {
    en: {
        translation: transEnglish,
    },

    fr: {
        translation: transFrench,
    }
}

i18next.use(initReactI18next).init({
    resources: resources,
    lng: "en",
});

export default i18next;
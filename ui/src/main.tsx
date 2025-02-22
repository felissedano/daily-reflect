import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.tsx'
import {Theme} from "@radix-ui/themes";
import "@radix-ui/themes/styles.css";
import './i18n.tsx';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
      <Theme accentColor={'gold'} appearance={'dark'}>
          <App />
      </Theme>
  </StrictMode>
)

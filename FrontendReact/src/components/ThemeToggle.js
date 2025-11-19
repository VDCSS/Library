import React, { useEffect, useState } from "react";

/**
 * ThemeToggle
 * - salva preferência em localStorage (key: 'theme-preference')
 * - respeita preferências do sistema (prefers-color-scheme) na primeira visita
 * - aplica/remover a classe "dark" no document.documentElement (ou body)
 */

const THEME_KEY = "theme-preference";

function getPreferredTheme() {
  const saved = localStorage.getItem(THEME_KEY);
  if (saved === "dark" || saved === "light") return saved;
  // respect system preference if no saved value
  if (window.matchMedia && window.matchMedia("(prefers-color-scheme: dark)").matches) {
    return "dark";
  }
  return "light";
}

export default function ThemeToggle() {
  const [theme, setTheme] = useState(getPreferredTheme());

  useEffect(() => {
    applyTheme(theme);
    const mq = window.matchMedia && window.matchMedia("(prefers-color-scheme: dark)");
    // listener to update if system theme changes and user hasn't explicitly chosen
    const onSysChange = (e) => {
      const saved = localStorage.getItem(THEME_KEY);
      if (!saved) {
        const sysTheme = e.matches ? "dark" : "light";
        setTheme(sysTheme);
        applyTheme(sysTheme);
      }
    };
    if (mq && mq.addEventListener) mq.addEventListener("change", onSysChange);
    else if (mq && mq.addListener) mq.addListener(onSysChange);
    return () => {
      if (mq && mq.removeEventListener) mq.removeEventListener("change", onSysChange);
      else if (mq && mq.removeListener) mq.removeListener(onSysChange);
    };
  }, []);

  function applyTheme(t) {
    const root = document.documentElement;
    if (t === "dark") {
      root.classList.add("dark");
    } else {
      root.classList.remove("dark");
    }
  }

  function toggleTheme() {
    const next = theme === "dark" ? "light" : "dark";
    setTheme(next);
    localStorage.setItem(THEME_KEY, next);
    applyTheme(next);
  }

  return (
    <button aria-label="Toggle theme" className="theme-toggle" onClick={toggleTheme}>
      {theme === "dark" ? (
        <>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" aria-hidden>
            <path d="M21 12.79A9 9 0 1111.21 3 7 7 0 0021 12.79z" fill="currentColor" />
          </svg>
          <span className="muted">Escuro</span>
        </>
      ) : (
        <>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" aria-hidden>
            <path d="M6.76 4.84l-1.8-1.79L3.17 4.84 4.98 6.63 6.76 4.84zM1 13h3v-2H1v2zm10 9h2v-3h-2v3zM17.24 19.16l1.8 1.79 1.79-1.79-1.79-1.79-1.8 1.79zM20 11v2h3v-2h-3zM12 4a8 8 0 100 16 8 8 0 000-16z" fill="currentColor"/>
          </svg>
          <span className="muted">Claro</span>
        </>
      )}
    </button>
  );
}

import React, { createContext, useContext, useState } from "react";

const AuthContext = createContext(null);
export function useAuth() {
    return useContext(AuthContext);
}

export function AuthProvider({ children }) {
    const [token, setToken] = useState(() => localStorage.getItem("token") || null);

    const saveToken = (t) => {
        setToken(t);
        t ? localStorage.setItem("token", t) : localStorage.removeItem("token");
    };

    const logout = () => saveToken(null);

    return (
        <AuthContext.Provider value={{ token, saveToken, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

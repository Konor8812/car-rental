// src/pages/Logout.jsx
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getToken, clearToken, logoutUser } from "../api";

export default function Logout() {
    const navigate = useNavigate();

    useEffect(() => {
        const token = getToken();
        if (token) {
            logoutUser(token).catch(() => {}); // ignore errors
            clearToken();
        }
        navigate("/");
    }, [navigate]);

    return (
        <div style={{ textAlign: "center", marginTop: "100px" }}>
            <p>Logging out...</p>
        </div>
    );
}

// src/api.js
import axios from "axios";

const API_BASE = "http://localhost:8081/api/v1";

export async function registerUser(email, password, username) {
    const res = await axios.post(`${API_BASE}/auth/register`, { email, password, username });
    return res.data.token;
}

export async function loginUser(email, password) {
    const res = await axios.post(`${API_BASE}/auth/login`, { email, password });
    return res.data.token;
}

export async function logoutUser(token) {
    await axios.post(`${API_BASE}/auth/logout`, null, {
        headers: { Authorization: `Bearer ${token}` },
    });
}

export function saveToken(token) {
    localStorage.setItem("authToken", token);
}

export function getToken() {
    return localStorage.getItem("authToken");
}

export function clearToken() {
    localStorage.removeItem("authToken");
}

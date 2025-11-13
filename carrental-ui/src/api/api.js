export const CORE_URL = "http://localhost:8080/api/v1/public";
export const AUTH_URL = "http://localhost:8081/api/v1";
export const PAYMENTS_URL = "http://localhost:8082/api/v1";

export async function api(baseUrl, path, { method = "GET", body, token, query } = {}) {
    const url = new URL(`${baseUrl}${path}`);
    if (query) Object.entries(query).forEach(([k, v]) => url.searchParams.append(k, v));
    const headers = { "Content-Type": "application/json" };
    if (token) headers["Authorization"] = token;
    const res = await fetch(url.toString(), { method, headers, body: body ? JSON.stringify(body) : undefined });
    if (res.status === 204) return null;
    const json = await res.json().catch(() => null);
    if (!res.ok) throw new Error(json?.message || `Request failed ${res.status}`);
    return json;
}

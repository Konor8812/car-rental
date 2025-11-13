import React, { useEffect, useState, createContext, useContext } from "react";
import { BrowserRouter, Routes, Route, Link, useNavigate, useParams } from "react-router-dom";

// React frontend for your car rental app with separated backend services:
// - Core service (cars, reservations, reviews): http://localhost:8080
// - Authorizer service (login, register, logout): http://localhost:8081
// - Payments service (createPaymentLink, completePayment): http://localhost:8082

const CORE_URL = "http://localhost:8080";
const AUTH_URL = "http://localhost:8081/api";
const PAYMENTS_URL = "http://localhost:8082";

async function api(baseUrl, path, { method = "GET", body, token, query } = {}) {
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

const AuthContext = createContext(null);
function useAuth() { return useContext(AuthContext); }
function AuthProvider({ children }) {
    const [token, setToken] = useState(() => localStorage.getItem("token") || null);
    const saveToken = (t) => { setToken(t); t ? localStorage.setItem("token", t) : localStorage.removeItem("token"); };
    const logout = () => saveToken(null);
    return <AuthContext.Provider value={{ token, saveToken, logout }}>{children}</AuthContext.Provider>;
}

export default function App_old() {
    return (
        <BrowserRouter>
            <AuthProvider>
                <div className="min-h-screen bg-gray-50">
                    <div className="max-w-5xl mx-auto p-6">
                        <Header />
                        <div className="mt-6">
                            <Routes>
                                <Route path="/" element={<Home />} />
                                <Route path="/auth" element={<AuthPage />} />
                                <Route path="/cars" element={<CarsList />} />
                                <Route path="/cars/:carId" element={<CarDetail />} />
                                <Route path="*" element={<NotFound />} />
                            </Routes>
                        </div>
                    </div>
                </div>
            </AuthProvider>
        </BrowserRouter>
    );
}

function Header() {
    const { token, logout } = useAuth();
    const navigate = useNavigate();
    return (
        <header className="flex items-center justify-between">
            <Link to="/" className="text-2xl font-semibold">CarRental</Link>
            <nav className="flex items-center gap-4">
                <Link to="/cars" className="px-3 py-1 rounded hover:bg-gray-100">Browse cars</Link>
                {token ? (
                    <button onClick={() => { logout(); navigate('/auth'); }} className="px-3 py-1 bg-red-500 text-white rounded">Logout</button>
                ) : (
                    <Link to="/auth" className="px-3 py-1 bg-blue-600 text-white rounded">Login / Register</Link>
                )}
            </nav>
        </header>
    );
}

function Home() {
    return (
        <div className="bg-white rounded-lg shadow p-6">
            <h1 className="text-xl font-bold">Welcome to CarRental</h1>
            <p className="mt-2 text-gray-600">Frontend demo connecting to 3 backend services on ports 8080, 8081, and 8082.</p>
        </div>
    );
}

function AuthPage() {
    const { saveToken } = useAuth();
    const [mode, setMode] = useState("login");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [username, setUsername] = useState("");
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    async function submit(e) {
        e.preventDefault();
        setError(null);
        try {
            if (mode === "login") {
                const res = await api(AUTH_URL, '/v1/auth/login', { method: 'POST', body: { email, password } });
                saveToken(res.token);
            } else {
                const res = await api(AUTH_URL, '/v1/auth/register', { method: 'POST', body: { email, password, username } });
                saveToken(res.token);
            }
            navigate('/cars');
        } catch (err) { setError(err.message); }
    }

    return (
        <div className="max-w-md mx-auto bg-white rounded shadow p-6">
            <h2 className="text-lg font-semibold">{mode === 'login' ? 'Login' : 'Register'}</h2>
            <form onSubmit={submit} className="mt-4 space-y-3">
                <input value={email} onChange={e => setEmail(e.target.value)} placeholder="Email" className="w-full p-2 border rounded" />
                <input value={password} onChange={e => setPassword(e.target.value)} placeholder="Password" type="password" className="w-full p-2 border rounded" />
                {mode === 'register' && <input value={username} onChange={e => setUsername(e.target.value)} placeholder="Username" className="w-full p-2 border rounded" />}
                {error && <div className="text-red-600">{error}</div>}
                <div className="flex items-center gap-2">
                    <button className="px-3 py-2 bg-indigo-600 text-white rounded" type="submit">{mode === 'login' ? 'Login' : 'Register'}</button>
                    <button type="button" onClick={() => setMode(mode === 'login' ? 'register' : 'login')} className="text-sm underline">Switch to {mode === 'login' ? 'Register' : 'Login'}</button>
                </div>
            </form>
        </div>
    );
}

function CarsList() {
    const { token } = useAuth();
    const [cars, setCars] = useState([]);
    const [page, setPage] = useState(0);
    const [size] = useState(10);
    const [total, setTotal] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => { load(); }, [page]);

    async function load() {
        setLoading(true); setError(null);
        try {
            const res = await api(CORE_URL, '/api/v1/cars', { query: { page, size }, token });
            setCars(res.cars || []);
            setTotal(res.totalElements || 0);
        } catch (err) { setError(err.message); } finally { setLoading(false); }
    }

    return (
        <div className="bg-white rounded shadow p-6">
            <h2 className="text-lg font-semibold">Available cars</h2>
            {loading ? <div>Loading...</div> : (
                <div className="mt-4 grid grid-cols-1 md:grid-cols-2 gap-4">
                    {cars.map(c => (
                        <div key={c.id} className="p-4 border rounded">
                            <div className="font-semibold">{c.manufacturer} {c.model} ({c.year})</div>
                            <div className="text-sm text-gray-600">Rate: {c.rentalDetails?.rate ?? '—'}</div>
                            <div className="mt-2 flex gap-2">
                                <button onClick={() => navigate(`/cars/${c.id}`)} className="px-3 py-1 border rounded">Details</button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
            {error && <div className="mt-4 text-red-600">{error}</div>}
            <div className="mt-6 flex gap-3">
                <button disabled={page === 0} onClick={() => setPage(p => Math.max(0, p - 1))} className="px-3 py-1 border rounded">Prev</button>
                <div>Page {page + 1}</div>
                <button disabled={(page + 1) * size >= total} onClick={() => setPage(p => p + 1)} className="px-3 py-1 border rounded">Next</button>
            </div>
        </div>
    );
}

function CarDetail() {
    const { token } = useAuth();
    const { carId } = useParams();
    const [car, setCar] = useState(null);
    const [available, setAvailable] = useState(false);
    const [paymentInfo, setPaymentInfo] = useState(null);
    const [score, setScore] = useState(5);
    const [comment, setComment] = useState("");
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => { load(); }, [carId]);

    async function load() {
        setLoading(true); setError(null);
        try {
            const res = await api(CORE_URL, `/api/v1/cars/${carId}`, { token });
            setCar(res.car); setAvailable(res.available);
        } catch (err) { setError(err.message); } finally { setLoading(false); }
    }

    async function reserve() {
        try {
            const res = await api(CORE_URL, `/api/v1/cars/${carId}/reserve`, { method: 'POST', token });
            setPaymentInfo(res);
        } catch (err) { setError(err.message); }
    }

    async function simulatePayment() {
        if (!paymentInfo?.paymentLink) return;
        const idMatch = paymentInfo.paymentLink.match(/(\d+)/);
        const paymentId = idMatch ? Number(idMatch[0]) : Date.now();
        await api(PAYMENTS_URL, '/api/v1/payments/complete', { method: 'POST', body: { paymentId, isSuccessful: true } });
        await load();
    }

    async function release() {
        await api(CORE_URL, `/api/v1/cars/${carId}/release`, { method: 'POST', token });
        await load();
    }

    async function addReview() {
        await api(CORE_URL, `/api/v1/cars/${carId}/review`, { method: 'POST', token, body: { score, comment } });
        setComment(''); await load();
    }

    if (loading) return <div className="p-6 bg-white rounded shadow">Loading...</div>;
    if (error) return <div className="p-6 bg-white rounded shadow text-red-600">{error}</div>;
    if (!car) return <div className="p-6 bg-white rounded shadow">No car found</div>;

    return (
        <div className="bg-white rounded shadow p-6">
            <h3 className="text-xl font-semibold">{car.manufacturer} {car.model} ({car.year})</h3>
            <div className="text-sm text-gray-600">Available: {available ? 'Yes' : 'No'}</div>
            <div className="mt-4 flex gap-2">
                <button onClick={reserve} className="px-4 py-2 bg-indigo-600 text-white rounded">Reserve</button>
                <button onClick={release} className="px-4 py-2 border rounded">Release</button>
            </div>
            {paymentInfo && (
                <div className="mt-4 p-3 border rounded bg-gray-50">
                    <div>Payment link: <a href={paymentInfo.paymentLink} className="underline" target="_blank" rel="noreferrer">{paymentInfo.paymentLink}</a></div>
                    <button onClick={simulatePayment} className="mt-2 px-3 py-1 bg-green-600 text-white rounded">Simulate payment</button>
                </div>
            )}
            <div className="mt-6">
                <h4 className="font-semibold">Reviews</h4>
                {car.reviews?.map((r, i) => <div key={i}>{r.score}/5 - {r.comment}</div>)}
                <div className="mt-3 flex gap-2">
                    <input type="number" min={1} max={5} value={score} onChange={e => setScore(Number(e.target.value))} className="w-20 p-2 border rounded" />
                    <input value={comment} onChange={e => setComment(e.target.value)} placeholder="Comment" className="flex-1 p-2 border rounded" />
                    <button onClick={addReview} className="px-3 py-1 bg-blue-600 text-white rounded">Add</button>
                </div>
            </div>
        </div>
    );
}

function NotFound() {
    return <div className="p-6 bg-white rounded shadow">Page not found</div>;
}

import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { api, CORE_URL } from "../api/api";
import { useAuth } from "../auth/AuthContext.jsx";

export default function CarsList() {
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
        setLoading(true);
        setError(null);
        try {
            const res = await api(CORE_URL, "/cars", { query: { page, size }, token });
            setCars(res.cars || []);
            setTotal(res.totalElements || 0);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="bg-white rounded shadow p-6">
            <h2 className="text-lg font-semibold">Available cars</h2>
            {loading ? (
                <div>Loading...</div>
            ) : (
                <div className="mt-4 grid grid-cols-1 md:grid-cols-2 gap-4">
                    {cars.map((c) => (
                        <div key={c.id} className="p-4 border rounded">
                            <div className="font-semibold">{c.manufacturer} {c.model} ({c.year})</div>
                            <div className="text-sm text-gray-600">Rate: {c.rentalDetails?.rate ?? "—"}</div>
                            <div className="mt-2 flex gap-2">
                                <button onClick={() => navigate(`/cars/${c.id}`)} className="px-3 py-1 border rounded">Details</button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
            {error && <div className="mt-4 text-red-600">{error}</div>}
            <div className="mt-6 flex gap-3">
                <button disabled={page === 0} onClick={() => setPage((p) => Math.max(0, p - 1))} className="px-3 py-1 border rounded">Prev</button>
                <div>Page {page + 1}</div>
                <button disabled={(page + 1) * size >= total} onClick={() => setPage((p) => p + 1)} className="px-3 py-1 border rounded">Next</button>
            </div>
        </div>
    );
}

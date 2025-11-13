import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext.jsx";
import { api, CORE_URL } from "../api/api";

export default function CarDetail() {
    const { token } = useAuth();
    const { carId } = useParams();
    const navigate = useNavigate();
    const [car, setCar] = useState(null);
    const [available, setAvailable] = useState(false);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => { load(); }, [carId]);

    async function load() {
        setLoading(true);
        setError(null);
        try {
            const res = await api(CORE_URL, `/cars/${carId}`, { token });
            setCar(res.car);
            setAvailable(res.available);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    }

    if (loading) return <div className="p-6 bg-white rounded shadow">Loading...</div>;
    if (error) return <div className="p-6 bg-white rounded shadow text-red-600">{error}</div>;
    if (!car) return <div className="p-6 bg-white rounded shadow">No car found</div>;

    return (
        <div className="bg-white rounded shadow p-6">
            <h3 className="text-xl font-semibold">
                {car.manufacturer} {car.model} ({car.year})
            </h3>
            <div className="text-sm text-gray-600">
                Available: {available ? "Yes" : "No"}
            </div>

            <div className="mt-4 flex gap-2">
                {available ? (
                    <button
                        onClick={() => navigate(`/cars/${carId}/reserve`)}
                        className="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700"
                    >
                        Reserve
                    </button>
                ) : (
                    <button
                        disabled
                        className="px-4 py-2 bg-gray-300 text-gray-600 rounded cursor-not-allowed"
                    >
                        Reserve (unavailable)
                    </button>
                )}
            </div>

            <div className="mt-6">
                <h4 className="font-semibold">Reviews</h4>
                {car.reviews?.length ? (
                    car.reviews.map((r, i) => (
                        <div key={i}>
                            {r.score}/5 - {r.comment}
                        </div>
                    ))
                ) : (
                    <div className="text-sm text-gray-500">No reviews yet.</div>
                )}
            </div>
        </div>
    );
}

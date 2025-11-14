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
    const [showReviewForm, setShowReviewForm] = useState(false);
    const [reviewScore, setReviewScore] = useState(5);
    const [reviewComment, setReviewComment] = useState("");
    const [submittingReview, setSubmittingReview] = useState(false);

    useEffect(() => { load(); }, [carId]);

    async function submitReview() {
        setSubmittingReview(true);
        setError(null);

        try {
            await api(CORE_URL, `/cars/${carId}/review`, {
                method: "POST",
                token,
                body: {
                    score: reviewScore,
                    comment: reviewComment,
                },
                headers: {
                    "Content-Type": "application/json",
                },
            });

            // reload car details to update reviews
            await load();

            // reset and close form
            setShowReviewForm(false);
            setReviewComment("");
            setReviewScore(5);
        } catch (err) {
            setError(err.message);
        } finally {
            setSubmittingReview(false);
        }
    }

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
                <div className="flex items-center justify-between">
                    <h4 className="font-semibold">Reviews</h4>

                    <button
                        onClick={() => setShowReviewForm((x) => !x)}
                        className="px-3 py-1 text-sm bg-green-600 text-white rounded hover:bg-green-700"
                    >
                        {showReviewForm ? "Cancel" : "Add Review"}
                    </button>
                </div>

                {showReviewForm && (
                    <div className="mt-3 p-4 border rounded bg-gray-50">
                        <label className="block mb-2 text-sm font-medium">
                            Score (1–5)
                            <select
                                className="block mt-1 border rounded p-1"
                                value={reviewScore}
                                onChange={(e) => setReviewScore(parseInt(e.target.value))}
                            >
                                {[1, 2, 3, 4, 5].map(n => (
                                    <option key={n} value={n}>{n}</option>
                                ))}
                            </select>
                        </label>

                        <label className="block mb-2 text-sm font-medium">
                            Comment
                            <textarea
                                className="block w-full mt-1 border rounded p-2"
                                rows={2}
                                value={reviewComment}
                                onChange={(e) => setReviewComment(e.target.value)}
                            />
                        </label>

                        <button
                            onClick={submitReview}
                            disabled={submittingReview}
                            className="px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700 disabled:bg-gray-400"
                        >
                            {submittingReview ? "Submitting..." : "Submit Review"}
                        </button>
                    </div>
                )}

                <div className="mt-4">
                    {car.reviews?.length ? (
                        car.reviews.map((r, i) => (
                            <div key={i} className="border-b py-2">
                                <strong>{r.score}/5</strong> — {r.comment}
                            </div>
                        ))
                    ) : (
                        <div className="text-sm text-gray-500">No reviews yet.</div>
                    )}
                </div>
            </div>
        </div>
    );
}

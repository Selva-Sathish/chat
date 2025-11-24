import {lazy} from "react";
import { createBrowserRouter } from "react-router-dom";


const Login = lazy(() => import("./pages/Login"));

const routes = createBrowserRouter([
    {
        path: "/login",
        element: <Login/>
    }
])

export default routes;
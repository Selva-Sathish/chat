import {lazy} from "react";
import { createBrowserRouter } from "react-router-dom";
import ProtectedRoute from "./routes/ProtectedRoute";

const Login = lazy(() => import("./pages/Login"));
const DemoLogin = lazy(() => import('./pages/DemoLogin'));
const Home = lazy(() => import('./pages/Home'))

const routes = createBrowserRouter([
    {
        path: "/login",
        element: <DemoLogin/>
    },
    
    {
        element: <ProtectedRoute/>,
        children: [
            {
                path: "",
                element: <Home />
            }
        ]
    }

])

export default routes;
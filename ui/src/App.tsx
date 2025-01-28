import {BrowserRouter, Routes, Route} from "react-router-dom";
import JournalPage from "./features/Journaling/JournalPage.tsx";
import AuthPage from "./core/auth/AuthPage.tsx";


function App() {
    return (
        <>
            <BrowserRouter>
                <Routes>
                    <Route index element={<JournalPage/>}/>
                    <Route path={"/home"} element={<JournalPage/>}/>

                    <Route path={"auth"}>
                        <Route path={"login"} element={<AuthPage/>}/>
                    </Route>
                </Routes>
            </BrowserRouter>
        </>

    );
}

export default App;

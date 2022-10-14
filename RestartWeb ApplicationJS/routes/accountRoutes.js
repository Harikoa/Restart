const express = require("express")
const router = express.Router()
const {addAcc,getAllAcc} = require("../controller/accountController")

router.get("/",getAllAcc)

router.post("/create",addAcc)
module.exports = router
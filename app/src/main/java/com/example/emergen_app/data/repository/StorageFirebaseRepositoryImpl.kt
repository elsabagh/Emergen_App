package com.example.emergen_app.data.repository


import android.util.Log
import com.example.emergen_app.data.models.Branch
import com.example.emergen_app.data.models.User
import com.example.emergen_app.domain.repository.StorageFirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageFirebaseRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : StorageFirebaseRepository {

    override suspend fun getUserRole(email: String): String? {
        return try {
            val querySnapshot = fireStore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()

            val role = if (!querySnapshot.isEmpty) {
                querySnapshot.documents[0].getString("role")
            } else {
                null
            }
            Log.d("FirebaseRepo", "User $email role: $role")
            role
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching user role", e)
            null
        }
    }

    override suspend fun getUserStatus(email: String): String? {
        return try {
            val querySnapshot = fireStore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()

            val status = if (!querySnapshot.isEmpty) {
                querySnapshot.documents[0].getString("statusAccount")
            } else {
                null
            }
            Log.d("FirebaseRepo", "User $email status: $status")
            status
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching user status", e)
            null
        }
    }

    override suspend fun getAllUsersWithStatus(status: String): List<User> {
        return try {
            val querySnapshot = fireStore.collection("users")
                .whereEqualTo("statusAccount", status)
                .whereEqualTo("role", "user")
                .get()
                .await()

            // إذا كانت النتيجة غير فارغة، طباعة الوثائق المسترجعة
            if (!querySnapshot.isEmpty) {
                querySnapshot.documents.forEach { document ->
                    Log.d("FirebaseRepo", "User Data: ${document.data}")
                }
            }

            querySnapshot.documents.mapNotNull { doc ->
                val user = doc.toObject(User::class.java)
                user?.copy(userId = doc.id)
            }
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching users with status: $status", e)
            emptyList<User>()
        }
    }

    override suspend fun getAllReports(): List<User> {
        return try {
            val querySnapshot = fireStore.collection("reports").get().await()
            querySnapshot.documents.mapNotNull { doc ->
                val request = doc.toObject(User::class.java)
                request?.copy(userId = doc.id)
            }
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching help requests: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getReportById(reportId: String): User? {
        return try {
            val reportSnapshot = fireStore.collection("reports").document(reportId).get().await()
            val report = reportSnapshot.toObject(User::class.java)
            report // إرجاع بيانات التقرير (المستخدم موجود داخل التقرير)
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching report by ID: ${e.message}")
            null
        }
    }

    override suspend fun getFilteredReportsByBranchType(typeBranch: String): List<User> {
        return try {
            val querySnapshot = fireStore.collection("reports")
                .whereEqualTo("typeOfRequest", typeBranch) // فلترة باستخدام typeBranch فقط
                .get()
                .await()

            querySnapshot.documents.mapNotNull { doc ->
                val request = doc.toObject(User::class.java)
                request?.copy(userId = doc.id)
            }
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching filtered help requests by branch type: ${e.message}")
            emptyList()
        }
    }



    override suspend fun getFilteredReportsByBranchTypeAndName(typeBranch: String, branchName: String): List<User> {
        return try {
            val querySnapshot = fireStore.collection("reports")
                .whereEqualTo("typeOfRequest", typeBranch) // فلترة باستخدام typeBranch
                .whereEqualTo("nameBranch", branchName)  // فلترة باستخدام branchName
                .get()
                .await()

            querySnapshot.documents.mapNotNull { doc ->
                val request = doc.toObject(User::class.java)
                request?.copy(userId = doc.id)
            }
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching filtered help requests by branch type and name: ${e.message}")
            emptyList()
        }
    }




    // جلب بيانات المستخدم من Firestore
    override fun getUserById(userId: String): Flow<User> {
        return flow {
            val userDoc = fireStore.collection("users").document(userId).get().await()
            val user = userDoc.toObject(User::class.java)
            emit(user ?: User())  // إرسال بيانات المستخدم
        }
    }

    override fun getBranchUserById(branchId: String): Flow<Branch> {
        return flow {
            val branchDoc = fireStore.collection("users").document(branchId).get().await()
            val branch = branchDoc.toObject(Branch::class.java)
            emit(branch ?: Branch())  // إرسال بيانات الفرع
        }
    }

    override suspend fun getBranchesByType(typeBranch: String): List<Branch> {
        return try {
            val querySnapshot = fireStore.collection("users")
                .whereEqualTo("role", "branch")  // ✅ جلب الفروع فقط
                .whereEqualTo("typeBranch", typeBranch)  // ✅ الفلاتر حسب `typeBranch`
                .get()
                .await()

            // إذا كانت النتيجة غير فارغة، طباعة الوثائق المسترجعة
            if (!querySnapshot.isEmpty) {
                querySnapshot.documents.forEach { document ->
                    Log.d("FirebaseRepo", "User Data: ${document.data}")
                }
            }

            querySnapshot.documents.mapNotNull { doc ->
                val branch = doc.toObject(Branch::class.java)
                branch?.copy(userId = doc.id)  // ✅ إضافة `userId` إلى بيانات الفرع
            }
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching branches of type: $typeBranch", e)
            emptyList<Branch>()
        }
    }

    override suspend fun deleteBranchById(branchId: String) {
        try {
            // حذف بيانات الفرع من Firestore
            val branchRef = fireStore.collection("users").document(branchId)

            // حذف جميع المستندات المتعلقة بالفرع مثل المساعدة أو التقارير إذا كانت موجودة
            val reportsQuery = fireStore.collection("reports")
                .whereEqualTo("branchId", branchId)
                .get()
                .await()

            reportsQuery.documents.forEach { report ->
                report.reference.delete().await()  // حذف التقارير المرتبطة بالفرع
            }

            // حذف بيانات الفرع
            branchRef.delete().await()

            Log.d("FirebaseRepo", "Branch $branchId deleted successfully.")
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error deleting branch: ${e.message}")
            throw Exception("Failed to delete branch: ${e.message}", e)
        }
    }


    override suspend fun getBranchById(branchId: String): Branch? {
        return try {
            val docSnapshot = fireStore.collection("users")
                .document(branchId)  // 🔥 جلب البيانات من الوثيقة التي تحتوي على branchId
                .get()
                .await()

            if (docSnapshot.exists()) {
                docSnapshot.toObject(Branch::class.java)
                    ?.copy(userId = branchId)  // ✅ تحويل البيانات إلى كائن Branch
            } else {
                null  // ❌ لا يوجد فرع بهذا الـ ID
            }
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching branch by ID: $branchId", e)
            null
        }
    }

    override suspend fun updateBranch(branch: Branch) {
        try {
            val branchRef = fireStore.collection("users").document(branch.userId)

            Log.d("FirebaseRepo", "Attempting to update branch: ${branch.userId}")
            Log.d("FirebaseRepo", "Data to be updated: $branch")

            // تحديث البيانات
            branchRef.update(
                mapOf(
                    "branchName" to branch.branchName,
                    "branchCapacity" to branch.branchCapacity,
                    "email" to branch.email,
                    "mobileNumber" to branch.mobileNumber,
                    "address" to branch.address,
                    "password" to branch.password,
                    "workDays" to branch.workDays,
                    "startTime" to branch.startTime,
                    "endTime" to branch.endTime,
                    "addressMaps" to "${branch.latitude},${branch.longitude}",
                    "latitude" to branch.latitude,
                    "longitude" to branch.longitude
                )
            ).await()

            Log.d("FirebaseRepo", "Branch ${branch.userId} updated successfully.")
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error updating branch ${branch.userId}: ${e.message}")
            throw Exception("Failed to update branch: ${e.message}", e)
        }
    }

    override suspend fun updateUserProfile(updatedUser: User) {
        try {
            val userRef = fireStore.collection("users").document(updatedUser.userId)

            userRef.update(
                mapOf(
                    "userName" to updatedUser.userName,
                    "email" to updatedUser.email,
                    "mobile" to updatedUser.mobile,
                    "governmentName" to updatedUser.governmentName,
                    "area" to updatedUser.area,
                    "plotNumber" to updatedUser.plotNumber,
                    "streetName" to updatedUser.streetName,
                    "buildNumber" to updatedUser.buildNumber,
                    "floorNumber" to updatedUser.floorNumber,
                    "apartmentNumber" to updatedUser.apartmentNumber,
                    "addressMaps" to updatedUser.addressMaps,
                    "userPhoto" to updatedUser.userPhoto,
                    "idFront" to updatedUser.idFront,
                    "idBack" to updatedUser.idBack,
                )
            ).await()
        } catch (e: Exception) {
            Log.e("ProfileDetailsViewModel", "Failed to update profile: ${e.message}")
            throw e
        }
    }

    override suspend fun createHelpRequest(user: User) {
        try {
            val db = FirebaseFirestore.getInstance()

            // إنشاء بيانات جديدة للتقرير
            val reportData = mapOf(
                "userId" to user.userId,
                "userName" to user.userName,
                "email" to user.email,
                "mobile" to user.mobile,
                "governmentName" to user.governmentName,
                "area" to user.area,
                "plotNumber" to user.plotNumber,
                "streetName" to user.streetName,
                "buildNumber" to user.buildNumber,
                "floorNumber" to user.floorNumber,
                "apartmentNumber" to user.apartmentNumber,
                "addressMaps" to user.addressMaps,
                "userPhoto" to user.userPhoto,
                "idFront" to user.idFront,
                "idBack" to user.idBack,
                "typeOfRequest" to user.typeOfRequest,
                "textOther" to user.textOther,
                "typeReason" to user.typeReason,
                "timeOfRequest" to user.timeOfRequest,
                "statusRequest" to user.statusRequest,
                "nameBranch" to user.nameBranch,
                "mobileBranch" to user.mobileBranch,
                "addressBranch" to user.addressBranch,
                "betweenAddress" to user.betweenAddress,
                "latitude" to user.latitude,
                "longitude" to user.longitude
            )

            db.collection("reports")
                .add(reportData)
                .await()

            Log.d("HelpRequest", "Urgent Help Request created successfully.")
        } catch (e: Exception) {
            Log.e("HelpRequest", "Error creating urgent help request: ${e.message}")
        }
    }

    override suspend fun getAllHelpRequests(): List<User> {
        return try {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid


            if (userId != null) {
                val querySnapshot = fireStore.collection("reports")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                querySnapshot.documents.mapNotNull { doc ->
                    val request = doc.toObject(User::class.java)
                    request?.copy(userId = doc.id)
                }
            } else {
                emptyList<User>()
            }
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error fetching help requests: ${e.message}")
            emptyList()
        }
    }

    override suspend fun updateReportStatus(userId: String, newStatus: String) {
        try {
            fireStore.collection("reports")
                .document(userId)
                .update("statusRequest", newStatus)
                .await()
            Log.d(
                "FirebaseRepo",
                "Successfully updated report status to $newStatus for user $userId"
            )
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Error updating report status: ${e.message}")
        }
    }

    override suspend fun updateReport(updatedReport: User) {
        try {
            val reportRef = fireStore.collection("reports").document(updatedReport.userId)

            reportRef.update(
                mapOf(
                    "userName" to updatedReport.userName,
                    "email" to updatedReport.email,
                    "mobile" to updatedReport.mobile,
                    "governmentName" to updatedReport.governmentName,
                    "area" to updatedReport.area,
                    "plotNumber" to updatedReport.plotNumber,
                    "streetName" to updatedReport.streetName,
                    "buildNumber" to updatedReport.buildNumber,
                    "floorNumber" to updatedReport.floorNumber,
                    "apartmentNumber" to updatedReport.apartmentNumber,
                    "addressMaps" to updatedReport.addressMaps,
                    "userPhoto" to updatedReport.userPhoto,
                    "idFront" to updatedReport.idFront,
                    "idBack" to updatedReport.idBack,
                    "typeOfRequest" to updatedReport.typeOfRequest,
                    "textOther" to updatedReport.textOther,
                    "typeReason" to updatedReport.typeReason,
                    "timeOfRequest" to updatedReport.timeOfRequest,
                    "statusRequest" to updatedReport.statusRequest,
                    "nameBranch" to updatedReport.nameBranch,
                    "mobileBranch" to updatedReport.mobileBranch,
                    "addressBranch" to updatedReport.addressBranch,
                    "betweenAddress" to updatedReport.betweenAddress
                )
            ).await()
        } catch (e: Exception) {
            Log.e("FirebaseRepo", "Failed to update report: ${e.message}")
            throw e
        }
    }


}
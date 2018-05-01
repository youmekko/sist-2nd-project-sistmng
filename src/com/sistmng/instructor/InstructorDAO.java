package com.sistmng.instructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sistmng.Current;
import com.sistmng.SQLConnection;

public class InstructorDAO {

	// 강사별 개인 정보 확인
	public List<Instructor> instructorInfo(String mid) {

		List<Instructor> instructorInfo = new ArrayList<>();
		
		/*SELECT i.mid, m.name_ , m.phone , m.ssn , i.instructorRegDate , c.subjectCode , s.subjectName
		FROM member_ m , instructor_ i, checkSubject_ c , subject_ s
		WHERE m.mid = i.mid
		AND i.mid = c.mid
		AND s.subjectCode = c.subjectCode*/

		// 강사명 / 주민번호 / 전화번호 / 강의 가능 목록
		String sql = "SELECT name_, phone, ssn, instructorRegDate, subjectCode, subjectName FROM InstructorInfoView WHERE mid = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = SQLConnection.connect();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, mid);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				Instructor i = new Instructor();

				i.setName_(rs.getString("name_"));

				i.setSsn(rs.getString("ssn"));

				i.setPhone(rs.getString("phone"));

				i.setSubjectCode(rs.getString("subjectCode"));
				i.setSubjectName(rs.getString("subjectName"));

				i.setInstructorRegDate(rs.getDate("instructorRegDate").toLocalDate());

				instructorInfo.add(i);

			}
			rs.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return instructorInfo;
	}

	// 강사별 과목 리스트
	public List<Instructor> subjectListByInstructor(String mid) {

		List<Instructor> subjectListByInstructor = new ArrayList<Instructor>();
		
		/*SELECT openS.openSubCode , subj.subjectName , openS.openSubStartDate , openS.openSubCloseDate , books.bookName , openC.openCoCode , cours.courseName , openC.openCoStartDate , openC.openCoCloseDate , clas.className,COUNT(*)OVER(PARTITION BY subj.subjectName) AS number_of_student, mem.mid, mem.name_
		FROM openSubject_ openS , subject_ subj , books_ books , course_ cours , openCourse_ openC , class_ clas , instructor_ instr , member_ mem
		WHERE openS.subjectCode = subj.subjectCode
		AND openS.openCoCode = openC.openCoCode
		AND openS.bookCode = books.bookCode
		AND openC.courseCode = cours.courseCode
		AND openC.classCode = clas.classCode
		AND openS.mid = instr.mid
		AND mem.mid = instr.mid
		GROUP BY openS.openSubCode , subj.subjectName , openS.openSubStartDate , openS.openSubCloseDate , books.bookName , openC.openCoCode , cours.courseName , openC.openCoStartDate , openC.openCoCloseDate , clas.className, mem.mid, mem.name_
		ORDER BY openS.openSubStartDate DESC*/

		// 과목코드 / 과목명 / 과목 시작일 / 과목 종료일 / 교재명/ 과정코드 / 과정명 / 과정 시작일/ 과정 종료일 / 강의실 / 등록인원 / 상태
		String sql = "SELECT openSubCode , subjectName , openSubStartDate , openSubCloseDate , bookName , openCoCode , courseName , openCoStartDate , openCoCloseDate , className, number_of_student FROM subjectListByInstructorView WHERE mid = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = SQLConnection.connect();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, Current.getInstance().getCurrent());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				Instructor i = new Instructor();

				i.setOpenSubCode(rs.getString("openSubCode"));
				i.setSubjectName(rs.getString("subjectName"));
				i.setOpenSubStartDate(rs.getDate("openSubStartDate").toLocalDate());
				i.setOpenSubCloseDate(rs.getDate("openSubCloseDate").toLocalDate());
				i.setBookName(rs.getString("bookName"));
				i.setOpenCocode(rs.getString("openCocode"));
				i.setCourseName(rs.getString("courseName"));
				i.setOpenCoStartDate(rs.getDate("openCoStartDate").toLocalDate());
				i.setOpenCoCloseDate(rs.getDate("openCoCloseDate").toLocalDate());
				i.setClassName(rs.getString("className"));
				i.setNumberOfStuHaveScore(rs.getInt("number_of_student"));

				subjectListByInstructor.add(i);

			}
			rs.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return subjectListByInstructor;

	}

	// 강사별 강의 가능한 과목
	public Instructor selectedSubjectByInstructor(String subCode) {
		Instructor i = new Instructor();
		
		/*SELECT openSubCode , subjectName , openSubStartDate , openSubCloseDate 
		FROM subject_ sub , openSubject_ openS , openCourse_ openC
		WHERE sub.subjectCode = openS.subjectCode
		GROUP BY openSubCode , subjectName , openSubStartDate , openSubCloseDate
		ORDER BY openSubCode ASC*/

		// 과목코드 / 과목명 / 과목시작일 / 과목종료일
		String sql = "SELECT openSubCode, subjectName, openSubStartDate, openSubCloseDate FROM selectedSubjectByInstructor WHERE openSubCode = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = SQLConnection.connect();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, subCode);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				i.setOpenSubCode(rs.getString("openSubCode"));
				i.setSubjectName(rs.getString("subjectName"));
				i.setOpenSubStartDate(rs.getDate("openSubStartDate").toLocalDate());
				i.setOpenSubCloseDate(rs.getDate("openSubCloseDate").toLocalDate());

			}
			rs.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return i;
	}

	// 과목별 학생 리스트
	public List<Instructor> studentListBySubject(String subCode) {

		List<Instructor> studentListBySubject = new ArrayList<Instructor>();
		
		
		/*SELECT m.mid, m.name_ ,m.phone, s.studentRegDate, openS.openSubCode ,openS.openSubStartDate , openS.openSubCloseDate, dropO.failureCode, dropO.failureDate
		FROM member_ m , student_ s, studentHistory_ sh , openCourse_ openC , openSubject_ openS , dropOut_ dropO
		WHERE m.mid = s.mid
		AND s.mid = sh.mid
		AND openC.openCoCode = sh.openCoCode
		AND openC.openCoCode = openS.openCoCode
		AND sh.openCoCode = dropO.openCoCode(+)
		AND sh.mid = dropO.mid(+)
		GROUP BY m.mid, m.name_ ,m.phone, s.studentRegDate, openS.openSubCode ,openS.openSubStartDate , openS.openSubCloseDate, dropO.failureCode ,dropO.failureDate*/

		// 회원코드 / 이름 / 전화번호 / 등록일 / 개설과목시작일 / 개설과목종료일 / 탈락코드 / 탈락 날짜 / 수료
		String sql = "SELECT mid, name_, phone, studentRegDate, openSubStartDate, openSubCloseDate, NVL(failureCode, 'noFailureCode') finalFailureCode, NVL(failureDate, '1901-01-01') finalFailureDate FROM studentListBySubjectView Where openSubCode = ? ORDER BY mid ASC";

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = SQLConnection.connect();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, subCode);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				Instructor i = new Instructor();

				i.setMid(rs.getString("mid"));
				i.setName_(rs.getString("name_"));
				i.setPhone(rs.getString("phone"));

				i.setOpenSubStartDate(rs.getDate("openSubStartDate").toLocalDate());
				i.setOpenSubCloseDate(rs.getDate("openSubCloseDate").toLocalDate());

				i.setStudentRegDate(rs.getDate("studentRegDate").toLocalDate());

				// 중도탈락코드 null이면 "noFailureCode"로 리턴
				i.setStudentStatus(rs.getString("finalFailureCode"));

				// 중도탈락날짜가 null이면
				i.setFailureDate(rs.getDate("finalFailureDate").toLocalDate());

				studentListBySubject.add(i);

			}
			rs.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return studentListBySubject;
	}

	// 종료된 과목 리스트
	public List<Instructor> subjectListWithTestInfo(String mid) {

		List<Instructor> subjectListWithTestInfo = new ArrayList<Instructor>();
		
		/*SELECT openS.openSubCode , subj.subjectName , openS.openSubStartDate , openS.openSubCloseDate , books.bookName , openC.openCoCode , cours.courseName , openC.openCoStartDate , openC.openCoCloseDate , clas.className,COUNT(*)OVER(PARTITION BY subj.subjectName) AS number_of_student, mem.mid, mem.name_
		FROM openSubject_ openS , subject_ subj , books_ books , course_ cours , openCourse_ openC , class_ clas , instructor_ instr , member_ mem
		WHERE openS.subjectCode = subj.subjectCode
		AND openS.openCoCode = openC.openCoCode
		AND openS.bookCode = books.bookCode
		AND openC.courseCode = cours.courseCode
		AND openC.classCode = clas.classCode
		AND openS.mid = instr.mid
		AND mem.mid = instr.mid
		GROUP BY openS.openSubCode , subj.subjectName , openS.openSubStartDate , openS.openSubCloseDate , books.bookName , openC.openCoCode , cours.courseName , openC.openCoStartDate , openC.openCoCloseDate , clas.className, mem.mid, mem.name_
		ORDER BY openS.openSubStartDate DESC*/

		// 개설 과목 코드, 과목명, 과목 시작일, 과목 종료일, 교재명, 개설 과정 코드, 과정명, 과정 시작일, 과정 종료일, 강의실명
		String sql = "SELECT openSubCode, subjectName, openSubStartDate, openSubCloseDate, bookName, openCoCode, courseName, openCoStartDate, openCoCloseDate, className FROM subjectListByInstructorView WHERE mid = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = SQLConnection.connect();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, mid);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				Instructor i = new Instructor();

				i.setOpenSubCode(rs.getString("openSubCode"));
				i.setSubjectName(rs.getString("subjectName"));
				i.setOpenSubStartDate(rs.getDate("openSubStartDate").toLocalDate());
				i.setOpenSubCloseDate(rs.getDate("openSubCloseDate").toLocalDate());

				i.setBookName(rs.getString("bookName"));

				i.setOpenCocode(rs.getString("openCocode"));
				i.setCourseName(rs.getString("courseName"));
				i.setOpenCoStartDate(rs.getDate("openCoStartDate").toLocalDate());
				i.setOpenCoCloseDate(rs.getDate("openCoCloseDate").toLocalDate());
				i.setClassName(rs.getString("className"));

				subjectListWithTestInfo.add(i);

			}
			rs.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return subjectListWithTestInfo;
	}

	// 과목별 시험 리스트
	public List<Instructor> testListBySubject(String openSubCode) {

		List<Instructor> testListBySubject = new ArrayList<>();
		
		/*SELECT os.openSubCode, s.subjectName, t.testCode, t.testDate, t.testFile, d.attDistribution, d.wriDistribution, d.pracDistribution
		FROM subject_ s, openSubject_ os, test_ t, distribution_ d
		WHERE s.subjectCode = os.subjectCode
		AND os.openSubCode = t.openSubCode
		AND t.testCode = d.testCode*/

		// 과목 코드, 과목명, 시험코드, 시험 날짜, 출석배점, 필기배점, 실기배점, 시험파일
		String sql = "SELECT openSubCode, subjectName, testCode, testDate, attDistribution, wriDistribution, pracDistribution, testFile FROM testsBySubjectView WHERE openSubCode=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = SQLConnection.connect();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, openSubCode);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				Instructor i = new Instructor();
				i.setSubjectCode(rs.getString("openSubCode"));
				i.setSubjectName(rs.getString("subjectName"));
				i.setTestCode(rs.getString("testCode"));
				i.setTestDate(rs.getDate("testDate").toLocalDate());
				i.setAttendanceDistribution(rs.getInt("attDistribution"));
				i.setWritingDistribution(rs.getInt("wriDistribution"));
				i.setPracticeDistribution(rs.getInt("pracDistribution"));
				i.setTestFile(rs.getString("testFile"));

				testListBySubject.add(i);

			}
			rs.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return testListBySubject;
	}

	// 시험(배점) 등록
	public int addDistribution(Instructor i) {

		int result1 = 0;
		int result2 = 0;

		String sql0 = "SELECT NVL(LPAD(SUBSTR(MAX(testCode),4,3)+1, 6, 'TES000'), 'TES001') finalTestCode FROM test_";

		String sql1 = "INSERT INTO test_ (testCode,openSubCode,testDate,testFile) VALUES (?,?,?,?)";

		String sql2 = "INSERT INTO distribution_ (disCode , testCode , attDistribution , wriDistribution , pracDistribution) VALUES ((SELECT NVL(LPAD(SUBSTR(MAX(disCode),4,3)+1, 6, 'DIV000'), 'DIV001') FROM distribution_), ?,?,?,?)";

		Connection conn = null;
		PreparedStatement pstmt0 = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;

		String testCode = "";

		try {
			conn = SQLConnection.connect();

			// 1번으로 추가한것
			conn.setAutoCommit(false);

			pstmt0 = conn.prepareStatement(sql0);

			ResultSet rs = pstmt0.executeQuery();

			while (rs.next()) {
				testCode = rs.getString("finalTestCode");
			}
			rs.close();

			pstmt1 = conn.prepareStatement(sql1);

			pstmt1.setString(1, testCode);
			pstmt1.setString(2, i.getOpenSubCode());
			pstmt1.setString(3, i.getTestDate().toString());
			pstmt1.setString(4, i.getTestFile());

			result1 = pstmt1.executeUpdate();

			pstmt2 = conn.prepareStatement(sql2);

			pstmt2.setString(1, testCode);
			pstmt2.setInt(2, i.getAttendanceDistribution());
			pstmt2.setInt(3, i.getWritingDistribution());
			pstmt2.setInt(4, i.getPracticeDistribution());

			result2 = pstmt2.executeUpdate();

			conn.commit();

		} catch (SQLException se) {
			se.printStackTrace();

			try {
				conn.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt1 != null)
					pstmt1.close();

			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return result1 + result2;
	}

	// 시험(배점) 삭제
	public int deleteTest(String testCode) {

		int result = 0;

		String sql = "DELETE test_ WHERE testCode = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = SQLConnection.connect();

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, testCode);

			result = pstmt.executeUpdate();

		} catch (SQLException se) {

			System.out.println(se.getMessage());

		} catch (Exception e) {
			// System.out.println(e.getMessage());

			System.out.println();
			System.out.println(e.getMessage());

		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {

			}

			try {
				SQLConnection.close();
			} catch (SQLException se) {
				System.out.println();
				System.out.println(se.getMessage());
			}
		}
		return result;
	}

	// 과목별 시험 리스트
	public List<Instructor> subjectListByNumberOfStudent(String mid) {

		List<Instructor> subjectListByNumberOfStudent = new ArrayList<>();
		
		
		/*SELECT ins.mid ,tes.testCode , tes.testDate ,dist.attDistribution , dist.wriDistribution , dist.pracDistribution ,openS.openSubCode ,sub.subjectName ,openS.openSubStartDate , openS.openSubCloseDate ,bok.bookName ,openC.openCoCode ,cou.courseName ,openC.openCoStartDate , openC.openCoCloseDate ,cla.className ,COUNT(sco.testCode) AS studentNumber
		FROM test_ tes , distribution_ dist , openSubject_ openS , subject_ sub ,books_ bok , openCourse_ openC , course_ cou , class_ cla , score_ sco , instructor_ ins
		WHERE tes.testCode = dist.testCode
		AND tes.openSubCode = openS.openSubCode
		AND sub.subjectCode = openS.subjectCode
		AND bok.bookCode = openS.bookCode
		AND openC.courseCode = cou.courseCode
		AND cla.classCode = openC.classCode
		AND openS.openCoCode = openC.openCoCode
		AND ins.mid = openS.mid
		GROUP BY ins.mid, tes.testCode , tes.testDate ,dist.attDistribution , dist.wriDistribution , dist.pracDistribution,openS.openSubCode,sub.subjectName,openS.openSubStartDate , openS.openSubCloseDate,bok.bookName,openC.openCoCode,cou.courseName,openC.openCoStartDate , openC.openCoCloseDate,cla.className
		ORDER BY tes.testCode ASC*/

		// 과목코드 / 과목명 / 과목 시작일 / 과목 종료일 / 교재명/ 과정코드 / 과정명 / 과정 시작일 / 과정 종료일 / 강의실 / 등록인원 / 상태
		String sql = "SELECT testcode, testdate, attDistribution, wriDistribution, pracDistribution, openSubCode, subjectName, openSubStartDate, openSubCloseDate, bookName, openCoCode, courseName, openCoStartDate, openCoCloseDate, className, studentNumber FROM testView WHERE mid = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = SQLConnection.connect();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, mid);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				Instructor i = new Instructor();

				i.setTestCode(rs.getString("testCode"));
				i.setTestDate(rs.getDate("testDate").toLocalDate());
				i.setAttendanceDistribution(rs.getInt("attDistribution"));
				i.setWritingDistribution(rs.getInt("wriDistribution"));
				i.setPracticeDistribution(rs.getInt("pracDistribution"));
				i.setOpenSubCode(rs.getString("openSubCode"));
				i.setSubjectName(rs.getString("subjectName"));
				i.setOpenSubStartDate(rs.getDate("openSubStartDate").toLocalDate());
				i.setOpenSubCloseDate(rs.getDate("openSubCloseDate").toLocalDate());
				i.setBookName(rs.getString("bookName"));
				i.setOpenCocode(rs.getString("openCocode"));
				i.setCourseName(rs.getString("courseName"));
				i.setOpenCoStartDate(rs.getDate("openCoStartDate").toLocalDate());
				i.setOpenCoCloseDate(rs.getDate("openCoCloseDate").toLocalDate());
				i.setClassName(rs.getString("className"));
				i.setNumberOfStuHaveScore(rs.getInt("studentNumber"));

				subjectListByNumberOfStudent.add(i);

			}
			rs.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return subjectListByNumberOfStudent;

	}

	// 시험별 학생 성적 리스트
	public List<Instructor> scoreInfoByStudents(String testCode) {
		List<Instructor> scoreInfoByStudents = new ArrayList<>();
		
		/*SELECT stt.testCode, stt.mid , mem.name_ , mem.phone , mem.memberRegDate,	
		sco.writingScore , sco.practiceScore, sco.attendanceScore, dro.failureDate, dro.failureCode	
		FROM (SELECT stu.mid, tes.testCode, tes.openSubCode	
		FROM student_ stu, test_ tes, studentHistory_ sh,  openCourse_ openC, openSubject_ openS	
		WHERE stu.mid = sh.mid	
		AND sh.openCoCode = openC.openCoCode	
		AND openC.openCoCode = openS.openCoCode	
		AND openS.openSubCode = tes.openSubCode	
		) stt,	
		member_ mem , score_ sco,	
		studentHistory_ sh, openCourse_ opc, openSubject_ ops, dropOut_ dro	
		WHERE mem.mid = stt.mid	
		AND stt.testCode = sco.testCode(+)	
		AND stt.mid = sco.mid(+)	
		AND sh.mid = stt.mid	
		AND sh.mid = dro.mid(+)	
		AND sh.openCoCode = dro.openCoCode(+)	
		AND opc.openCoCode = sh.openCoCode	
		AND stt.openSubCode = ops.openSubCode	
		AND opc.openCoCode = ops.openCoCode	
		GROUP BY sco.scoreCode, stt.testCode, stt.mid , mem.name_ , mem.phone ,	
		mem.memberRegDate, sco.attendanceScore , sco.writingScore , sco.practiceScore, dro.failureDate, dro.failureCode	
		ORDER BY stt.testCode ASC*/

		// 회원코드 / 이름 / 전화번호 / 등록일 / 수료 / 출결점수 / 필기점수 / 실기점수 / 총점"
		String sql = "SELECT mid, name_, phone, memberRegDate, NVL(failureCode, 'noFailureCode') finalFailureCode, NVL(failureDate, '1901-01-01') finalFailureDate, attendanceScore, writingScore, practiceScore FROM testStudentView WHERE testCode = ? ORDER BY mid ASC";

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = SQLConnection.connect();
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, testCode);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				Instructor i = new Instructor();

				i.setMid(rs.getString("mid"));
				i.setName_(rs.getString("name_"));
				i.setPhone(rs.getString("phone"));
				i.setStudentRegDate(rs.getDate("memberRegDate").toLocalDate());

				// 중도탈락코드 null이면 "1901-01-01"로 리턴

				// 중도탈락코드 null이면 "noFailureCode"로 리턴
				i.setStudentStatus(rs.getString("finalFailureCode"));

				// 중도탈락날짜가 null이면
				i.setFailureDate(rs.getDate("finalFailureDate").toLocalDate());

				i.setAttendanceScore(rs.getInt("attendanceScore"));
				i.setWritingScore(rs.getInt("writingScore"));
				i.setPracticeScore(rs.getInt("practiceScore"));

				scoreInfoByStudents.add(i);

			}
			rs.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return scoreInfoByStudents;
	}

	// 성적 등록
	public int addScore(Instructor i) {

		int result = 0;

		String sql = "INSERT INTO score_ (scoreCode , mid , testCode , attendanceScore , writingScore, practiceScore) VALUES ((SELECT NVL(LPAD(SUBSTR(MAX(scoreCode),4,3)+1,6,'GRA000'),'GRA001') FROM score_),?,?,?,?,?)";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = SQLConnection.connect();

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, i.getMid());
			pstmt.setString(2, i.getTestCode());
			pstmt.setInt(3, i.getAttendanceScore());
			pstmt.setInt(4, i.getWritingScore());
			pstmt.setInt(5, i.getPracticeScore());

			result = pstmt.executeUpdate();

		} catch (SQLException se) {

			System.out.println(se.getMessage());

		} catch (Exception e) {

			System.out.println(e.getMessage());

		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				System.out.println(se.getMessage());
			}
		}

		return result;
	}

	// 성적 삭제
	public int deleteScore(String mid, String testCode) {
		int result = 0;

		String sql = "DELETE FROM score_ WHERE testCode = ? AND mid = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = SQLConnection.connect();

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, testCode);
			pstmt.setString(2, mid);

			result = pstmt.executeUpdate();

		} catch (SQLException se) {

			System.out.println(se.getMessage());

		} catch (Exception e) {

			System.out.println(e.getMessage());

		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException se) {
			}
			try {
				SQLConnection.close();
			} catch (SQLException se) {
				System.out.println(se.getMessage());
			}
		}

		return result;
	}

}
